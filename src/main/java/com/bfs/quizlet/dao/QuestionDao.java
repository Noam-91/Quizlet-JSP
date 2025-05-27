package com.bfs.quizlet.dao;


import com.bfs.quizlet.domain.Question;
import com.bfs.quizlet.orm.QuestionORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QuestionORM questionORM;
    @Autowired
    public QuestionDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, QuestionORM questionORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.questionORM = questionORM;
    }

    public List<Question> getQuestionsByCategoryId(Long categoryId){
        String sql = "SELECT * FROM question WHERE category_id = ?";
        return jdbcTemplate.query(sql, questionORM, categoryId);
    }
    public List<Question> getActiveQuestionsByCategoryId(Long categoryId){
        String sql = "SELECT * FROM question WHERE category_id = ? AND is_active = true";
        return jdbcTemplate.query(sql, questionORM, categoryId);
    }

    public Optional<Question> getQuestionById(Long questionId){
        String sql = "SELECT * FROM question WHERE question_id = ?";
        try{
            Question question = jdbcTemplate.queryForObject(sql, questionORM, questionId);
            return Optional.of(question);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Integer getActiveQuestionCount(){
        String sql = "SELECT COUNT(*) FROM question WHERE is_active = true";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Long insertQuestion(Question question){
        String sql = "INSERT INTO question (category_id, description, created_by) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, question.getCategoryId(), question.getDescription(), question.getCreatedBy());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public void toggleQuestionStatus(Long questionId, Long userId){
        String sql = "UPDATE question SET is_active = NOT is_active, updated_by = ? WHERE question_id = ?";
        jdbcTemplate.update(sql, userId, questionId);
    }

    public void updateQuestion(Question question, Long updatedBy){
        String sql = "UPDATE question SET description = ?, updated_by = ? WHERE question_id = ?";
        jdbcTemplate.update(sql, question.getDescription(), updatedBy, question.getQuestionId());
    }
}
