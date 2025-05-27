package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.QuizQuestion;
import com.bfs.quizlet.orm.QuizQuestionORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class QuizQuestionDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QuizQuestionORM quizQuestionORM;
    @Autowired
    public QuizQuestionDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, QuizQuestionORM quizQuestionORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.quizQuestionORM = quizQuestionORM;
    }

    public void addQuizQuestions(List<QuizQuestion> quizQuestions) {
        String sql = "INSERT INTO quizquestion (quiz_id, question_id) VALUES (?, ?)";
        for (QuizQuestion quizQuestion : quizQuestions) {
            jdbcTemplate.update(sql, quizQuestion.getQuizId(), quizQuestion.getQuestionId());
        }
    }

    public List<Long> getQuestionIdsByQuizId(Long quizId){
        String sql = "SELECT question_id FROM quizquestion WHERE quiz_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, quizId);
    }

    public Optional<QuizQuestion> getQuizQuestionByQuizIdAndQuestionIndex(Long quizId, Long questionIndex){
        String sql = "SELECT * FROM quizquestion WHERE quiz_id = ? AND question_id = ?";
        List<QuizQuestion> quizQuestions = jdbcTemplate.query(sql, quizQuestionORM, quizId, questionIndex);
        if(quizQuestions.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(quizQuestions.get(0));
    }

    public List<QuizQuestion>findByQuizId(Long quizId){
        String sql = "SELECT * FROM quizquestion WHERE quiz_id = ?";
        return jdbcTemplate.query(sql, quizQuestionORM, quizId);
    }

    public List<QuizQuestion> getAllActiveQuizQuestions() {
        String sql = "SELECT * FROM quizquestion WHERE is_active = true";
        return jdbcTemplate.query(sql, quizQuestionORM);
    }

}
