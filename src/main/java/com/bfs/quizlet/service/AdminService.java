package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.*;
import com.bfs.quizlet.domain.Quiz;
import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.dto.AdminStatsDTO;
import com.bfs.quizlet.dto.Page;
import com.bfs.quizlet.dto.QuestionDTO;
import com.bfs.quizlet.dto.UserManageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final QuizDao quizDao;
    private final QuizService quizService;
    private final CategoryDao categoryDao;
    public AdminService(UserDao userDao, QuestionDao questionDao, QuizDao quizDao, QuizService quizService, CategoryDao categoryDao) {
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.quizDao = quizDao;
        this.quizService = quizService;
        this.categoryDao = categoryDao;
    }

    /** get admin stats
     * 1. get user count
     * 2. get question count
     * 3. get completed quiz count
     * 4. get average quiz score, using prepareQuestionsForQuiz!
     * 5. get active category count
     * 6. get most popular category name
     * @return AdminStatsDTO
    **/
    public AdminStatsDTO getAdminStats() {
        int userCount = userDao.getActiveUserCount();

        int questionCount = questionDao.getActiveQuestionCount();

        List<Quiz> completedQuizzes = quizDao.getAllInactiveQuizzes();

        int completedQuizCount = completedQuizzes.size();

        float totalScore = completedQuizzes.stream().map(quiz -> {
            List<QuestionDTO> questions = quizService.prepareQuestionsForQuiz(quiz.getQuizId());
            long correctQuestionCount = questions.stream().filter(QuestionDTO::getIsCorrect).count();
            if (!questions.isEmpty()) {
                return (float) correctQuestionCount / questions.size();
            } else {
                return 0.0f;
        }
        }).reduce(Float::sum).orElse(0.0f);
        float averageQuizScore = totalScore / completedQuizzes.size();

        int activeCategoryCount = categoryDao.getActiveCategoryCount();
        String mostPopularCategoryName = quizDao.findMostPopularCategoryName().orElse("No popular category found");

        return AdminStatsDTO.builder()
                .userCount(userCount)
                .questionCount(questionCount)
                .completedQuizCount(completedQuizCount)
                .averageQuizScore(averageQuizScore)
                .activeCategoryCount(activeCategoryCount)
                .mostPopularCategoryName(mostPopularCategoryName)
                .build();
    }

    public Page<UserManageDTO> getAllUsersInPage(int currentPage, int pageSize) {
        List<User> users = userDao.getAllUsers();
        List<UserManageDTO> allUsers= users.stream().map(user -> UserManageDTO.builder()
                .userId(user.getUserId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .isAdmin(user.getIsAdmin())
                .build())
                .collect(Collectors.toList());
        return Page.getOnePageContent(allUsers, currentPage, pageSize);
    }

    public List<UserManageDTO> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        return users.stream().map(user -> UserManageDTO.builder()
                .userId(user.getUserId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .build())
                .collect(Collectors.toList());
    }
}
