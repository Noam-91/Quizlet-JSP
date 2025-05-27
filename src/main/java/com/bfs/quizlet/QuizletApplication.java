package com.bfs.quizlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// Entry point of the application.
@SpringBootApplication
@ServletComponentScan
public class QuizletApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizletApplication.class, args);
    }

}
