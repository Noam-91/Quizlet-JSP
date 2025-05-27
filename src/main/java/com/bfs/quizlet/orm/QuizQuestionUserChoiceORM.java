package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.QuizQuestionUserChoice;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QuizQuestionUserChoiceORM implements RowMapper<QuizQuestionUserChoice>{
    @Override
    public QuizQuestionUserChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new QuizQuestionUserChoice(
                rs.getLong("qquc_id"),
                rs.getLong("qq_id"),
                rs.getLong("choice_id")
        );
    }
}
