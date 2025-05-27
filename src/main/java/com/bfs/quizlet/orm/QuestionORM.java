package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.Question;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QuestionORM implements RowMapper<Question>{

    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        Question question = new Question();
        question.setQuestionId(rs.getLong("question_id"));
        question.setCategoryId(rs.getLong("category_id"));
        question.setDescription(rs.getString("description"));
        question.setIsActive(rs.getBoolean("is_active"));
        question.setCreatedAt(rs.getTimestamp("created_at"));
        question.setUpdatedAt(rs.getTimestamp("updated_at"));
        question.setCreatedBy(rs.getObject("created_by", Long.class)); // Handle nullable Long
        question.setUpdatedBy(rs.getObject("updated_by", Long.class));
        return question;
    }
}
