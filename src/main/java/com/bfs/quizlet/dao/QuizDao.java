package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.Quiz;
import com.bfs.quizlet.orm.QuizORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class QuizDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QuizORM quizORM;
    @Autowired
    public QuizDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, QuizORM quizORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.quizORM = quizORM;
    }
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(QuizDao.class);

    public Long createNewQuiz(Quiz quiz){
        String sql = "INSERT INTO quiz (category_id, user_id, name, duration) VALUES (:category_id, :user_id, :name, :duration)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("category_id", quiz.getCategoryId());
            params.addValue("user_id", quiz.getUserId());
            params.addValue("name", quiz.getName());
            params.addValue("duration", quiz.getDuration());
            namedParameterJdbcTemplate.update(sql, params, keyHolder);
            logger.info("New quiz created successfully: {}", quiz);
            if(keyHolder.getKey()!=null){
                return keyHolder.getKey().longValue();
            }else{
                throw new SQLException("Failed to retrieve auto-generated key for new quiz.");
            }
        }catch (Exception e){
            logger.error("Error creating new quiz: {}", e.getMessage());
            throw new RuntimeException("Failed to create new quiz", e); // Re-throw or handle appropriately
        }
    }

    public Optional<Quiz> getQuizById(Long id) {
        String query = "SELECT * FROM quiz WHERE quiz_id = ?";

        try{
            Quiz quiz = jdbcTemplate.queryForObject(query, quizORM, id);
            logger.info("Retrieved quiz by id: {}", id);
            return Optional.of(quiz);
        }catch (Exception e) {
            logger.error("Error retrieving quiz by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get all quizzes order by created_at in descending order
     * @return List of quizzes
    **/
    public List<Quiz> getAllQuizzesForUser(Long userId) {
        String query = "SELECT * FROM quiz WHERE user_id = :userId ORDER BY created_at DESC";
        try{
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("userId", userId);
            List<Quiz> quizzes = namedParameterJdbcTemplate.query(query, params, quizORM);
            logger.info("Retrieved all quizzes");
            return quizzes;
        }catch (Exception e){
            logger.error("Error retrieving all quizzes: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve all quizzes", e);
        }
    }

    public void deactivate(Long quizId) {
        String sql = "UPDATE quiz SET is_active = 0 WHERE quiz_id = ?";
        try{
            jdbcTemplate.update(sql, quizId);
            logger.info("Quiz deactivated successfully: {}", quizId);
        }catch (Exception e){
            logger.error("Error deactivating quiz: {}", e.getMessage());
            throw new RuntimeException("Failed to deactivate quiz", e);
        }

    }

    public List<Quiz> getAllInactiveQuizzes() {
        String query = "SELECT * FROM quiz WHERE is_active = false";
        try{
            List<Quiz> quizzes = jdbcTemplate.query(query, quizORM);
            logger.info("Retrieved all inactive quizzes");
            return quizzes;
        }catch (Exception e){
            logger.error("Error retrieving all inactive quizzes: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve all inactive quizzes", e);
        }
    }

    public Optional<String> findMostPopularCategoryName() {
        String query = "SELECT c.name FROM quiz q JOIN category c ON q.category_id = c.category_id GROUP BY c.category_id ORDER BY COUNT(*) DESC LIMIT 1";
        try {
            String categoryName = jdbcTemplate.queryForObject(query, String.class);
            logger.info("Retrieved most popular category name: {}", categoryName);
            return Optional.of(categoryName);
        } catch (Exception e) {
            logger.error("Error retrieving most popular category name: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
