package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.orm.UserORM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserORM userORM;
    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, UserORM userORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.userORM = userORM;
    }

    public void createNewUser(User user) {
        String query = "INSERT INTO user (email,password,firstname,lastname) VALUES (?, ?, ?, ?)";
        try{
            jdbcTemplate.update(query,
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname());
            logger.info("New user created: {}", user.getEmail());
        }catch (Exception e) {
            logger.error("Error creating new user: {}", e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            User user = jdbcTemplate.queryForObject(query, userORM, email);
            logger.info("Retrieved user by email: {}", email);
            return user;
        }catch (Exception e) {
            logger.error("Error retrieving user by email: {}", e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM user";
        try{
            List<User> users = jdbcTemplate.query(query, userORM);
            logger.info("Retrieved all users");
            return users;
        }catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage());
            return null;
        }
    }

    public Integer getActiveUserCount() {
        String query = "SELECT COUNT(*) FROM user WHERE is_active = true";
        try{
            Integer count = jdbcTemplate.queryForObject(query, Integer.class);
            logger.info("Retrieved active user count: {}", count);
            return count;
        }catch (Exception e) {
            logger.error("Error retrieving active user count: {}", e.getMessage());
            return null;
        }
    }

    public void toggleUserStatus(Long userId, Long updatedBy){
        String query = "UPDATE user SET is_active = NOT is_active, updated_by = ? WHERE user_id = ?";
        try{
            jdbcTemplate.update(query, updatedBy, userId);
            logger.info("Toggled user status for user id: {}", userId);
        }catch (Exception e) {
            logger.error("Error toggling user status: {}", e.getMessage());
        }
    }

    public Optional<User> getUserByUserId(Long userId) {
        String query = "SELECT * FROM user WHERE user_id = ?";
        try{
            User user = jdbcTemplate.queryForObject(query, userORM, userId);
            logger.info("Retrieved user by user id: {}", userId);
            return Optional.ofNullable(user);
        }catch (Exception e) {
            logger.error("Error retrieving user by user id: {}", e.getMessage());
            return Optional.empty();
        }
    }

}
