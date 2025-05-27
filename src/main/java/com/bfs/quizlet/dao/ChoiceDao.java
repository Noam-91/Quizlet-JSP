package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.Choice;
import com.bfs.quizlet.orm.ChoiceORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChoiceDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ChoiceORM choiceORM;
    @Autowired
    public ChoiceDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ChoiceORM choiceORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.choiceORM = choiceORM;
    }

    public List<Choice> getChoicesByQuestionId(Long questionId){
        String sql = "SELECT * FROM choice WHERE question_id = ? AND is_active = TRUE";
        return jdbcTemplate.query(sql, choiceORM, questionId);
    }

    public void updateChoices(List<Choice> choices, Long updatedBy){
        String sql = "UPDATE choice SET description = ?, is_correct = ?, is_active = ?, updated_by = ? WHERE choice_id = ?";
        for (Choice choice : choices) {
            jdbcTemplate.update(sql, choice.getDescription(), choice.getIsCorrect(),
                    choice.getIsActive(), updatedBy, choice.getChoiceId());
        }
    }

    public void insertChoice(Choice choice, Long createdBy){
        String sql = "INSERT INTO choice (question_id, description, is_correct, created_by) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, choice.getQuestionId(), choice.getDescription(), choice.getIsCorrect(), createdBy);
    }
}
