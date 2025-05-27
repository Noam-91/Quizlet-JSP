package com.bfs.quizlet.dao;

import com.bfs.quizlet.domain.Category;
import com.bfs.quizlet.orm.CategoryORM;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CategoryORM categoryORM;
    @Autowired
    public CategoryDao(JdbcTemplate jdbcTemplate,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                       CategoryORM categoryORM) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.categoryORM = categoryORM;
    }
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CategoryDao.class);

    /** get all categories
     * order by created_at in descending order + updated_at in descending order
     * to keep the new category on top
     * @return List of categories
     * */
    public List<Category> getAllCategories() {
        String query = "SELECT * FROM category ORDER BY created_at DESC, updated_at DESC";
        try {
            return jdbcTemplate.query(query, categoryORM);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public Optional<Category> getCategoryById(Long id) {
        String query = "SELECT * FROM category WHERE category_id = ?";
        try {
            Category category = jdbcTemplate.queryForObject(query, categoryORM, id);
            logger.info("Retrieved category by id: {}", id);
            return Optional.of(category);
        } catch (Exception e) {
            logger.error("Error retrieving category by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Integer getActiveCategoryCount(){
        String query = "SELECT COUNT(*) FROM category WHERE is_active = true";
        try{
            Integer count = jdbcTemplate.queryForObject(query, Integer.class);
            logger.info("Retrieved active category count: {}", count);
            return count;
        }catch (Exception e) {
            logger.error("Error retrieving active category count: {}", e.getMessage());
            return null;
        }
    }

    public Long createCategory(String name, Long userId) {
        String query = "INSERT INTO category (name, created_by, updated_by) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, name, userId, userId);
            logger.info("Created category: {}", name);
            return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        } catch (Exception e) {
            logger.error("Error creating category: {}", e.getMessage());
            return null;
        }
    }

    public void toggleCategoryStatus(Long categoryId, Long userId) {
        String query = "UPDATE category SET is_active = NOT is_active, updated_by = ? WHERE category_id = ?";
        try {
            jdbcTemplate.update(query, userId, categoryId);
            logger.info("Toggled category status: {}", categoryId);
        } catch (Exception e) {
            logger.error("Error toggling category status: {}", e.getMessage());
        }
    }

    public void renameCategory(Long categoryId, String newName, Long userId){
        String query = "UPDATE category SET name = ?, updated_by = ? WHERE category_id = ?";
        try {
            jdbcTemplate.update(query, newName, userId, categoryId);
            logger.info("Renamed category: {}", categoryId);
        } catch (Exception e) {
            logger.error("Error renaming category: {}", e.getMessage());
        }
    }
}
