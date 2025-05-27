package com.bfs.quizlet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatsDTO {
    private int userCount;
    private int questionCount;
    private int completedQuizCount;
    private double averageQuizScore;
    private int activeCategoryCount;
    private String mostPopularCategoryName;
}
