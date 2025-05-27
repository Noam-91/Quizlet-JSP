package com.bfs.quizlet.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
