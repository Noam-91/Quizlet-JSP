package com.bfs.quizlet.controller;

import com.bfs.quizlet.domain.*;
import com.bfs.quizlet.service.QuestionService;
import com.bfs.quizlet.service.QuizService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private QuizService quizService;
    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(QuizController.class);

    @PostMapping("/new")
    public String startNewQuiz(@RequestParam(name = "categoryId") Long categoryId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Long quizId = quizService.startNewQuiz(categoryId,user.getUserId());
        if(quizId==null){
            logger.error("Error creating new quiz");
            return "redirect:/home";
        }
        logger.debug("Starting new quiz for category: {}, user: {}", categoryId, user.getUserId());
        // redirect to the first question of the quiz
        return "redirect:/quiz/"+quizId+"/0";
    }

    /** Return the quiz based on quizId
    *   REGARDLESS whether it expires
    *   If not exists, redirect to Error
    */
    @GetMapping("/{quizId}/{currentQuestionIndex}")
    public String getQuiz(@PathVariable(name = "quizId") Long quizId,
                           @PathVariable(name = "currentQuestionIndex") int currentQuestionIndex,
                           Model model) {
        logger.info("Get request quiz: {}", quizId);
        Optional<Quiz> op = quizService.getQuizById(quizId);
        if(op.isPresent()){
            Quiz quiz = op.get();
            if(!quiz.getIsActive()){
                logger.warn("Quiz is not active: {}", quizId);
            }
            // calculate time left
            Long timeLeft = quizService.calculateTimeLeft(quizId)/1000;
            model.addAttribute("timeLeft", timeLeft);
            model.addAttribute("quiz", quiz);
            model.addAttribute("currentQuestionIndex", currentQuestionIndex);
            logger.info("Quiz found: {}", quiz);
        }else{
            logger.error("Quiz not found: {}", quizId);
            return "error";
        }
        return "quiz";
    }

    @PostMapping("/{quizId}")
    public String submitQuiz(
            @PathVariable(name = "quizId") Long quizId,
            @RequestParam(name = "questionId") Long questionId,
            @RequestParam(name = "questionIndex") Integer questionIndex,
            @RequestParam(name = "userChoices", defaultValue = "") List<Long> userChoices
    ) {
        Optional<QuizQuestion> op = quizService.getQuizQuestionByQuizIdAndQuestionIndex(quizId, questionId);
        if(op.isPresent()){
            QuizQuestion quizQuestion = op.get();
            quizService.submitQuestionChoice(quizQuestion.getQuestionId(), quizQuestion.getQqId(), userChoices);
        }else{
            logger.error("QuizQuestion not found: {}", quizId);
        }
        // If it is the last page, redirect to home.
        List<Long> questionIds = quizService.getQuestionIdsByQuizId(quizId);
        if(questionIndex==questionIds.size()-1){
            quizService.deactivateQuiz(quizId);
            return "redirect:/home";
        }
        return "redirect:/quiz/"+quizId+"/"+(questionIndex+1);
    }
}
