package com.bfs.quizlet.controller;

import com.bfs.quizlet.domain.Category;
import com.bfs.quizlet.domain.Quiz;
import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.service.CategoryService;
import com.bfs.quizlet.service.QuizService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    private final CategoryService categoryService;
    private final QuizService quizService;
    @Autowired
    public HomeController(CategoryService categoryService, QuizService quizService) {
        this.categoryService = categoryService;
        this.quizService = quizService;
    }
    Logger logger = org.slf4j.LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String getHome(Model model, HttpServletRequest request) {
        try{
            List<Category> categories = categoryService.getAllCategories();
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            List<Quiz> quizzes = quizService.getTenQuizzes(user.getUserId());
            model.addAttribute("categories", categories);
            model.addAttribute("quizzes", quizzes);
        }catch (Exception e){
            logger.error("Error getting quizzes: {}", e.getMessage());
        }

        return "home";
    }

}
