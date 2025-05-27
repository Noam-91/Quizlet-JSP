package com.bfs.quizlet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestionUserChoice {
    private Long qqucId;
    private Long qqId;
    private Long choiceId;
}
