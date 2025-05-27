package com.bfs.quizlet.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User{
    private Long userId;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private Boolean isAdmin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long updatedBy;

}
