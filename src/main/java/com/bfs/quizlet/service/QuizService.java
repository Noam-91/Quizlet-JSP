package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.ChoiceDao;
import com.bfs.quizlet.dao.QuizDao;
import com.bfs.quizlet.dao.QuizQuestionDao;
import com.bfs.quizlet.dao.QuizQuestionUserChoiceDao;
import com.bfs.quizlet.domain.*;
import com.bfs.quizlet.dto.Page;
import com.bfs.quizlet.dto.QuestionDTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizDao quizDao;
    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final QuizQuestionDao quizQuestionDao;
    private final QuizQuestionUserChoiceDao quizQuestionUserChoiceDao;
    private final ChoiceDao choiceDao;
    private final UserService userService;

    @Autowired
    public QuizService(QuizDao quizDao, CategoryService categoryService, QuestionService questionService, QuizQuestionDao quizQuestionDao, QuizQuestionUserChoiceDao quizQuestionUserChoiceDao, ChoiceDao choiceDao, UserService userService) {
        this.quizDao = quizDao;
        this.categoryService = categoryService;
        this.questionService = questionService;
        this.quizQuestionDao = quizQuestionDao;
        this.quizQuestionUserChoiceDao = quizQuestionUserChoiceDao;
        this.choiceDao = choiceDao;
        this.userService = userService;
    }
    Logger logger = org.slf4j.LoggerFactory.getLogger(QuizService.class);
    private static final int DEFAULT_DURATION = 5;             // 5 min limited for quiz
    private static final String DEFAULT_QUIZ_NAME = "New Quiz";
    private static final int DEFAULT_QUESTION_COUNT = 3;        // three questions per quiz

    /** start new quiz
     * 1. create new quiz
     * 2. create quiz questions
     * @param categoryId
     * @param userId
     * @return quizId
    **/
    @Transactional
    public Long startNewQuiz(Long categoryId, Long userId) {
        Quiz quiz = Quiz.builder()
                .categoryId(categoryId)
                .userId(userId)
                .name(DEFAULT_QUIZ_NAME)
                .duration(DEFAULT_DURATION)
                .build();
        Long quizId = 0L;
        try{
            quizId = quizDao.createNewQuiz(quiz);
            createQuizQuestions(quizId, categoryId, DEFAULT_QUESTION_COUNT);
            return quizId;
        }catch (Exception e){
            logger.error("Error creating new quiz: {}", e.getMessage());
            throw new RuntimeException("Failed to create new quiz", e);
        }
    }

    /** get quiz by id
     * 1. find quiz by id
     * 2. set category name
     * 3. set questions
     * 4. set correctQuestionCount
     * @param id
     * @return Quiz
    **/
    @Transactional
    public Optional<Quiz> getQuizById(Long id) {
        logger.info("Getting quiz by id: {}", id);
        Optional<Quiz> op = quizDao.getQuizById(id);
        if(op.isPresent()) {
            Quiz quiz = op.get();
            logger.info("Successfully retrieved quiz with id: {}", id);
            // set categoryName
            String categoryName = categoryService.getCategoryNameById(quiz.getCategoryId());
            quiz.setCategoryName(categoryName);
            // set creatorName
            String creatorName = userService.getFullNameById(quiz.getUserId());
            quiz.setCreatorName(creatorName);
            // set questions
            List<QuestionDTO> questions = prepareQuestionsForQuiz(id);
            quiz.setQuestions(questions);
            // set correctQuestionCount
            int correctQuestionCount = (int) questions.stream().filter(QuestionDTO::getIsCorrect).count();
            quiz.setCorrectQuestionCount(correctQuestionCount);
            return Optional.of(quiz);
        }
        logger.warn("Quiz with id {} not found", id);
        return op;
    }

    public Long calculateTimeLeft(Long quizId){
        Optional<Quiz> op = quizDao.getQuizById(quizId);
        if(op.isPresent()){
            Quiz quiz = op.get();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            long diff = quiz.getCreatedAt().getTime() + TimeUnit.MINUTES.toMillis(quiz.getDuration()) - now.getTime();
            return diff;
        }
        return 0L;
    }

    /** prepare questions for quiz
     * 1. find all questions in the quiz
     * 2. find all choices for each question
     * 3. find all user choices for each question
     * 4. check all selected choices and set isCorrect for each question
     * @param quizId
     * @return List of questions
    **/
    @Transactional
    public List<QuestionDTO> prepareQuestionsForQuiz(Long quizId) {
        List<QuizQuestion> quizQuestions = quizQuestionDao.findByQuizId(quizId); // Assumes a method like this in your DAO/Repo
        List<QuestionDTO> questionsForQuiz = new ArrayList<>();

        for(QuizQuestion qq : quizQuestions){
            // find all questions in the quiz.
            QuestionDTO question = questionService.getQuestionById(qq.getQuestionId())
                    .orElse(new QuestionDTO(qq.getQuestionId()));
            // find all choices for each question
            List<Choice> choices = choiceDao.getChoicesByQuestionId(qq.getQuestionId());
            question.setChoices(choices);
            // find all user choices for each question
            List<Long> userChoicesIds = quizQuestionUserChoiceDao.getUserChoicesByQqId(qq.getQqId());
            question.setUserChoicesIds(userChoicesIds);
            // set isCorrect
            boolean areAllSelectedChoicesCorrect = userChoicesIds.stream()
                    .allMatch(choiceId -> choices.stream()
                            .filter(choice -> choice.getChoiceId().equals(choiceId))
                            .findFirst()
                            .map(Choice::getIsCorrect)
                            .orElse(false));
            boolean areAllCorrectChoicesSelected =
                    choices.stream().filter(Choice::getIsCorrect).count() == userChoicesIds.size();
            question.setIsCorrect(areAllSelectedChoicesCorrect&&areAllCorrectChoicesSelected);
            questionsForQuiz.add(question);
        }
        return questionsForQuiz;
    }

    /**
     * 1. Get all quizzes owned by user ordered by created_at in descending order
     * 2. Assign category name to each quiz
     * 3. Expires the quiz if time is up
     * @param userId
     * @return List of quizzes
    **/
    @Transactional
    public List<Quiz> getAllQuizzesForUser(Long userId) {
        List<Quiz> quizzes = quizDao.getAllQuizzesForUser(userId);
        if(quizzes.isEmpty()){
            logger.warn("No quizzes found");
        }
        for (Quiz quiz : quizzes) {
            // Assign category Name
            String categoryName = categoryService.getCategoryNameById(quiz.getCategoryId());
            quiz.setCategoryName(categoryName);
            // Check if quiz is expired
            if(quiz.getIsActive()){
                quiz.setIsActive(checkAndExpireQuiz(quiz.getQuizId()));
            }
        }
        return quizzes;
    }

    @Transactional
    public List<Quiz> getTenQuizzes(Long userId) {
        List<Quiz> allQuizzes = getAllQuizzesForUser(userId);
        return allQuizzes.subList(0, Math.min(allQuizzes.size(), 10));
    }

    /** check if quiz is expired
     * 1. find quiz by id
     * 2. check if quiz is expired
     * 3. if expired, deactivate quiz
     * @param quizId
     * @return boolean
    **/
    @Transactional
    public boolean checkAndExpireQuiz(Long quizId) {
        Optional<Quiz> op = getQuizById(quizId);
        if(op.isPresent()) {
            Quiz quiz = op.get();
            Timestamp createdAt = quiz.getCreatedAt();

            long durationMillis = TimeUnit.MINUTES.toMillis(quiz.getDuration());
            long expirationTimeMillis = createdAt.getTime() + durationMillis;
            long currentTimeMillis = new Date().getTime();

            // Check if the current time is on or after the expiration time
            if(currentTimeMillis >= expirationTimeMillis) {
                logger.info("Quiz {} has expired. Deactivating...", quizId);
                quizDao.deactivate(quizId);
                return false;
            }
            return true;
        }else {
            logger.error("Quiz not found: {}", quizId);
            return false;
        }
    }
    /** create quiz questions
     * 1. find all active questions in the category
     * 2. randomly pick questions
     * 3. add to DB
     * @param quizId
     * @param categoryId
     * @param questionNum
     * @return List of quiz questions
    **/
    @Transactional
    public List<QuizQuestion> createQuizQuestions(Long quizId, Long categoryId, Integer questionNum) {
        List<Question> questions = questionService.getActiveQuestionsByCategoryId(categoryId);

        if (questions.isEmpty()) {
            logger.warn("No questions found for category: {}", categoryId); // Changed to WARN
            throw new RuntimeException("No questions found for category: " + categoryId);
        }

        // Randomly pick questions
        int numToSelect = Math.min(questions.size(), questionNum);
        Collections.shuffle(questions);
        List<QuizQuestion> quizQuestionsToInsert = questions.stream()
                .limit(numToSelect)
                .map(question -> QuizQuestion.builder()
                        .quizId(quizId)
                        .questionId(question.getQuestionId())
                        .build())
                .collect(Collectors.toList());
        logger.debug("Selected {} questions for quiz {}", quizQuestionsToInsert.size(), quizId);
        // Add to DB
        quizQuestionDao.addQuizQuestions(quizQuestionsToInsert);
        logger.debug("Inserted {} quiz questions into DB", quizQuestionsToInsert.size());
        return quizQuestionsToInsert;
    }

    public Optional<QuizQuestion> getQuizQuestionByQuizIdAndQuestionIndex(Long quizId, Long questionId){
        return quizQuestionDao.getQuizQuestionByQuizIdAndQuestionIndex(quizId, questionId);
    }
    public List<Long> getQuestionIdsByQuizId(Long quizId){
        return quizQuestionDao.getQuestionIdsByQuizId(quizId);
    }

    /** submit question choice
     * 1. find all choices for each question
     * 2. remove the unselected and add the new
     * @param questionId
     * @param qqId
     * @param userChoicesIds
    **/
    @Transactional
    public void submitQuestionChoice(Long questionId, Long qqId, List<Long> userChoicesIds){
        logger.debug("userChoicesIds: {}", userChoicesIds);
        // find all choices for each question
        List<Choice> choices = choiceDao.getChoicesByQuestionId(questionId);
        // remove the unselected and add the new
        for(Choice choice : choices){
            if(userChoicesIds.contains(choice.getChoiceId())){
                updateQuizQuestionUserChoice(qqId, choice.getChoiceId());
            }else{
                quizQuestionUserChoiceDao.deleteQuizQuestionUserChoice(qqId, choice.getChoiceId());
            }
        }
        logger.info("Submitted question choice: {}", questionId);
    }

    /** update quiz question user choice
     * 1. find quiz question user choice
     * 2. if exists, update
     * 3. if not, insert
     * @param qqId
     * @param choiceId
    **/
    @Transactional
    public void updateQuizQuestionUserChoice(Long qqId, Long choiceId){
        Optional<QuizQuestionUserChoice> qquc = quizQuestionUserChoiceDao.findByQqIdAndChoiceId(qqId, choiceId);
        if(qquc.isPresent()){
            quizQuestionUserChoiceDao.updateQuizQuestionUserChoice(qqId, choiceId);
            logger.debug("Updated quiz question user choice: {}", qqId);
        }else{
            quizQuestionUserChoiceDao.insertQuizQuestionUserChoice(qqId, choiceId);
            logger.debug("Inserted quiz question user choice: {}", qqId);
        }
    }

    @Transactional
    public void deactivateQuiz(Long quizId){
        quizDao.deactivate(quizId);
        logger.info("Deactivated quiz: {}", quizId);
    }

    @Transactional
    public Page<Quiz> pageFilterAndSort(int currentPage, int pageSize,
                                        Long categoryId, Long createdBy, String sortBy){
        List<Quiz> filteredQuizResults = filterAllQuizResultsByCategoryAndCreator(categoryId, createdBy);
        switch(sortBy){
            case "creatorName":
                filteredQuizResults.sort(Comparator.comparing(Quiz::getCreatorName));
                break;
            case "categoryName":
                filteredQuizResults.sort(Comparator.comparing(Quiz::getCategoryName));
                break;
            default:
                filteredQuizResults.sort(Comparator.comparing(Quiz::getCreatedAt).reversed());
        }
        return Page.getOnePageContent(filteredQuizResults, currentPage, pageSize);
    }

    @Transactional
    public List<Quiz> filterAllQuizResultsByCategoryAndCreator(Long categoryId, Long createdBy) {
        List<Quiz> quizResults = quizDao.getAllInactiveQuizzes();
        return quizResults.stream().map(qr->getQuizById(qr.getQuizId()))
                .filter(Optional::isPresent) // Keep only quizzes that were successfully fetched
                .map(Optional::get)
                .filter(quiz -> {
                    boolean matchesCategory = categoryId == 0L||
                            (quiz.getCategoryId() != null && quiz.getCategoryId() == categoryId);
                    boolean matchesCreator = createdBy == 0L ||
                            (quiz.getUserId() != null && quiz.getUserId() == createdBy);
                    return matchesCategory && matchesCreator;
        }).collect(Collectors.toList());
    }
}
