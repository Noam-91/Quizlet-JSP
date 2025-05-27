package com.bfs.quizlet.domain;

import com.bfs.quizlet.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {
    private Long quizId;
    private Long userId;
    private Long categoryId;
    private String name;
    private Integer duration;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Additional field
    private String categoryName;
    private String creatorName;
    private List<QuestionDTO> questions;
    private Integer correctQuestionCount;
}
