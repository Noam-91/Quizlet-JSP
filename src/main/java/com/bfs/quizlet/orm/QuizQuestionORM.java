package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.QuizQuestion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class QuizQuestionORM implements RowMapper<QuizQuestion> {
    @Override
    public QuizQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new QuizQuestion(
                rs.getLong("qq_id"),
                rs.getLong("quiz_id"),
                rs.getLong("question_id")
        );
    }
}
