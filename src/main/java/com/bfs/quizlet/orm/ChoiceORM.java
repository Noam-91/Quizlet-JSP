package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.Choice;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ChoiceORM implements RowMapper<Choice> {

    @Override
    public Choice mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Choice(
                rs.getLong("choice_id"),
                rs.getLong("question_id"),
                rs.getString("description"),
                rs.getBoolean("is_active"),
                rs.getBoolean("is_correct"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getLong("created_by"),
                rs.getLong("updated_by")
        );
    }
}
