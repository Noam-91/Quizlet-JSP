package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.User;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class UserORM implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getBoolean("is_active"),
                rs.getBoolean("is_admin"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getLong("updated_by")
        );
    }
}
