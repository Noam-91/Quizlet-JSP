package com.bfs.quizlet.controller;

import com.bfs.quizlet.domain.Quiz;
import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.service.QuizService;
import com.bfs.quizlet.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class QuizResultController {
    private final QuizService quizService;
    @Autowired
    public QuizResultController(QuizService quizService) {
        this.quizService = quizService;
    }
    Logger logger = org.slf4j.LoggerFactory.getLogger(QuizResultController.class);

    @GetMapping("/quiz-result/{quizId}")
    public String getQuizResult(@PathVariable(name = "quizId") Long quizId, Model  model) {
        logger.info("Getting quiz result: {}", quizId);
        Optional<Quiz> op = quizService.getQuizById(quizId);

        if(op.isPresent()){
            Quiz quiz = op.get();
            model.addAttribute("quiz", quiz);
            logger.info("Quiz found: {}", quiz);
        }else{
            logger.error("Quiz not found: {}", quizId);
            return "error";
        }
        return "quiz-result";
    }
}
