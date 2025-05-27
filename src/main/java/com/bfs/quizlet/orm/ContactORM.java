package com.bfs.quizlet.orm;

import com.bfs.quizlet.domain.Contact;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ContactORM implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Contact(
                rs.getLong("contact_id"),
                rs.getString("subject"),
                rs.getString("message"),
                rs.getString("email"),
                rs.getBoolean("is_active"),
                rs.getBoolean("is_solved"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getLong("updated_by")
        );
    }
}
