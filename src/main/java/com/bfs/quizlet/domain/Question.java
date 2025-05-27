package com.bfs.quizlet.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {
    private Long questionId;
    private Long categoryId;
    private String description;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public Question(Long questionId) {
        this.questionId = questionId;
        this.description = "Current question is not available";
    }

    public Question(Long categoryId, Long createdBy) {
        this.categoryId = categoryId;
        this.createdBy = createdBy;
        this.description = "Please describe the question.";
        this.isActive = true;
    }
}
