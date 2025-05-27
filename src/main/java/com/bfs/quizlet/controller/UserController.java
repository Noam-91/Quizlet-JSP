package com.bfs.quizlet.controller;

import com.bfs.quizlet.dto.UserRegisterDto;
import com.bfs.quizlet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterDto userRegisterDto) {
        logger.debug("Registering user: {}", userRegisterDto);
        try{
            userService.createNewUser(userRegisterDto);
        }catch (Exception e){
            logger.error("Error registering user: {}", e.getMessage());
        }
        return "redirect:/login";
    }

}
