package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.Quiz;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class QuizORM implements RowMapper<Quiz> {
    @Override
    public Quiz mapRow(ResultSet rs, int rowNum) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setQuizId(rs.getLong("quiz_id"));
        quiz.setUserId(rs.getLong("user_id"));
        quiz.setCategoryId(rs.getLong("category_id"));
        quiz.setName(rs.getString("name"));
        quiz.setDuration(rs.getInt("duration"));
        quiz.setIsActive(rs.getBoolean("is_active"));
        quiz.setCreatedAt(rs.getTimestamp("created_at"));
        quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
        return quiz;
    }
}
