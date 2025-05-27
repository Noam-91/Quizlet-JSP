package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.ChoiceDao;
import com.bfs.quizlet.dao.QuestionDao;
import com.bfs.quizlet.domain.Choice;
import com.bfs.quizlet.domain.Question;
import com.bfs.quizlet.dto.Page;
import com.bfs.quizlet.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionDao questionDao;
    private final ChoiceDao choiceDao;
    @Autowired
    public QuestionService(QuestionDao questionDao, ChoiceDao choiceDao) {
        this.questionDao = questionDao;
        this.choiceDao = choiceDao;
    }
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(QuestionService.class);

    public List<Question> getActiveQuestionsByCategoryId(Long categoryId) {
        return questionDao.getActiveQuestionsByCategoryId(categoryId);
    }

    public Optional<QuestionDTO> getQuestionById(Long questionId) {
        List<Choice> choices = choiceDao.getChoicesByQuestionId(questionId);
        Optional<Question> op = questionDao.getQuestionById(questionId);
        if(op.isPresent()) {
            QuestionDTO question = new QuestionDTO(op.get());
            question.setChoices(choices);
            logger.info("Successfully retrieved choices for question: {}", questionId);
            return Optional.of(question);
        }

        return Optional.empty();
    }

    public Page<QuestionDTO> getPageOfQuestionsByCategoryId(Long categoryId, int currentPage, int pageSize) {
        List<Question> questions = questionDao.getQuestionsByCategoryId(categoryId);
        List<QuestionDTO> questionDTOs = questions.stream().map(question -> {
            List<Choice> choices = choiceDao.getChoicesByQuestionId(question.getQuestionId());
            QuestionDTO questionDTO = new QuestionDTO(question);
            questionDTO.setChoices(choices);
            return questionDTO;
        }).collect(Collectors.toList());
        return Page.getOnePageContent(questionDTOs, currentPage, pageSize);
    }

    @Transactional
    public Long createQuestion(Long categoryId, Long userId){
        Question question = new Question(categoryId, userId);
        return questionDao.insertQuestion(question);
    }

    @Transactional
    public void toggleQuestionStatus(Long questionId, Long updatedBy) {
        questionDao.toggleQuestionStatus(questionId, updatedBy);
    }

    @Transactional
    public void updateQuestion(QuestionDTO questionDTO, Long updatedBy) {
        for(Choice choice : questionDTO.getChoices()){
            if(choice.getIsActive() == null){
                choice.setIsActive(false);
            }
            if(choice.getIsCorrect() == null){
                choice.setIsCorrect(false);
            }
        }
        questionDao.updateQuestion(questionDTO, updatedBy);
        choiceDao.updateChoices(questionDTO.getChoices(), updatedBy);
    }

    @Transactional
    public void addChoiceForQuestion(Long questionId, Long createdBy) {
        Choice choice = new Choice(questionId, createdBy);
        choiceDao.insertChoice(choice, createdBy);
    }

}
