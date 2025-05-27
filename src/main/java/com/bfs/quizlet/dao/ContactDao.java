package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.Contact;
import com.bfs.quizlet.orm.ContactORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ContactDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ContactORM contactORM;
    @Autowired
    public ContactDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ContactORM contactORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.contactORM = contactORM;
    }

    public void addContact(String subject, String message, String email) {
        String sql = "INSERT INTO contact (subject, message, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, subject, message, email);
    }

    public List<Contact> getAllActiveContactsOrderByIsSolvedANDCreatedAt() {
        String sql = "SELECT * FROM contact WHERE is_active = TRUE ORDER BY is_solved , created_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Contact contact = new Contact();
            contact.setContactId(rs.getLong("contact_id"));
            contact.setSubject(rs.getString("subject"));
            contact.setMessage(rs.getString("message"));
            contact.setEmail(rs.getString("email"));
            contact.setIsActive(rs.getBoolean("is_active"));
            contact.setIsSolved(rs.getBoolean("is_solved"));
            contact.setCreatedAt(rs.getTimestamp("created_at"));
            contact.setUpdatedAt(rs.getTimestamp("updated_at"));
            contact.setUpdatedBy(rs.getLong("updated_by"));
            return contact;
        });
    }

    public Optional<Contact> findById(Long contactId) {
        String sql = "SELECT * FROM contact WHERE contact_id = :contactId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("contactId", contactId);
        Contact contact = namedParameterJdbcTemplate.queryForObject(sql,params,contactORM);
        return Optional.ofNullable(contact);
    }

    public void updateContact(Long contactId, boolean isActive, boolean isSolved, Long updatedBy) {
        String sql = "UPDATE contact SET is_active = ?, is_solved = ?, updated_by = ? WHERE contact_id = ?";
        jdbcTemplate.update(sql, isActive, isSolved, updatedBy, contactId);
    }
}
