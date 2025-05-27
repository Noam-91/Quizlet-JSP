package com.bfs.quizlet.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Choice {
    private Long choiceId;
    private Long questionId;
    private String description;
    private Boolean isActive;
    private Boolean isCorrect;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public Choice(Long questionId, Long createdBy) {
        this.questionId = questionId;
        this.createdBy = createdBy;
        this.description = "Please describe the choice.";
        this.isActive = true;
        this.isCorrect = false;
    }
}