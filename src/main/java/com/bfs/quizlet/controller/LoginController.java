package com.bfs.quizlet.controller;

import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        // redirect to /quiz if user is already logged in
        if (session != null && session.getAttribute("user") != null) {
            return "redirect:/quiz";
        }
        return "login";
    }

    // validate that we are always getting a new session after login
    @PostMapping("/login")
    public String postLogin(@RequestParam String username,
                            @RequestParam String password,
                            HttpServletRequest request) {

        Optional<User> possibleUser = loginService.validateLogin(username, password);

        if(possibleUser.isPresent()) {
            HttpSession oldSession = request.getSession(false);
            // invalidate old session if it exists
            if (oldSession != null) oldSession.invalidate();

            // generate new session
            HttpSession newSession = request.getSession(true);

            // store user details in session
            newSession.setAttribute("user", possibleUser.get());

            return "redirect:/home";
        } else { // if user details are invalid
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession oldSession = request.getSession(false);
        // invalidate old session if it exists
        if(oldSession != null) oldSession.invalidate();
        return "login";
    }

    // If there are something wrong, will be redirected to "/error"
}
