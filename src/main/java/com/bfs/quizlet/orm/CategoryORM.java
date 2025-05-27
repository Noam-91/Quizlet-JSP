package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryORM implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Category(
                rs.getLong("category_id"),
                rs.getString("name"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getLong("created_by"),
                rs.getLong("updated_by")
        );
    }
}
