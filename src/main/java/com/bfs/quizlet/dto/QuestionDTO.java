package com.bfs.quizlet.dto;

import com.bfs.quizlet.domain.Choice;
import com.bfs.quizlet.domain.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QuestionDTO extends Question {
    // Additional fields
    private List<Choice> choices;
    private List<Long> userChoicesIds;
    private Boolean isCorrect;

    public QuestionDTO(Question question) {
        super(question.getQuestionId(), question.getCategoryId(), question.getDescription(), question.getIsActive(), question.getCreatedAt(), question.getUpdatedAt(), question.getCreatedBy(), question.getUpdatedBy());
        this.choices = new ArrayList<>();
        this.userChoicesIds = new ArrayList<>();
    }
    public QuestionDTO(Long questionId) {
        super(questionId);
        this.choices = new ArrayList<>();
        this.userChoicesIds = new ArrayList<>();
    }
}
