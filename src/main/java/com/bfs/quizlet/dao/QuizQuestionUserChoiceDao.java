package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.QuizQuestionUserChoice;
import com.bfs.quizlet.orm.QuizQuestionUserChoiceORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class QuizQuestionUserChoiceDao {
    private final JdbcTemplate jdbcTemplate;
    private final QuizQuestionUserChoiceORM quizQuestionUserChoiceORM;
    @Autowired
    public QuizQuestionUserChoiceDao(JdbcTemplate jdbcTemplate, QuizQuestionUserChoiceORM quizQuestionUserChoiceORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.quizQuestionUserChoiceORM = quizQuestionUserChoiceORM;
    }

    public List<Long> getUserChoicesByQqId(Long qqId){
        String sql = "SELECT choice_id FROM QuizQuestionUserChoice WHERE qq_id = ?";
        return jdbcTemplate.query(sql, new Object[]{qqId}, (rs, rowNum) -> rs.getLong("choice_id"));
    }

    public Optional<QuizQuestionUserChoice> findByQqIdAndChoiceId(Long qqId, Long choiceId){
        String sql = "SELECT * FROM QuizQuestionUserChoice WHERE qq_id = ? AND choice_id = ?";
        try{
            QuizQuestionUserChoice quizQuestionUserChoice = jdbcTemplate.queryForObject(sql, quizQuestionUserChoiceORM, qqId, choiceId);
            return Optional.of(quizQuestionUserChoice);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public void insertQuizQuestionUserChoice(Long qqId, Long choiceId){
        String sql = "INSERT INTO QuizQuestionUserChoice (qq_id, choice_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, qqId, choiceId);
    }

    public void updateQuizQuestionUserChoice(Long qqId, Long choiceId){
        String sql = "UPDATE QuizQuestionUserChoice SET choice_id = ? WHERE qq_id = ?";
        jdbcTemplate.update(sql, choiceId, qqId);
    }

    public void deleteQuizQuestionUserChoice(Long qqId, Long choiceId){
        String sql = "DELETE FROM QuizQuestionUserChoice WHERE qq_id = ? AND choice_id = ?";
        jdbcTemplate.update(sql, qqId, choiceId);
    }
}
