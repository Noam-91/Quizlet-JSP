package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.CategoryDao;
import com.bfs.quizlet.domain.Category;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;
    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CategoryService.class);

    public List<Category> getAllCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        if(categories.isEmpty()){
            logger.warn("No categories found");
        }
        return categories;
    }

    public Optional<Category> getCategoryById(Long id) {
        logger.info("Getting category by id: {}", id);
        return categoryDao.getCategoryById(id);
    }

    public String getCategoryNameById(Long id) {
        Optional<Category> category = categoryDao.getCategoryById(id);
        if(category.isPresent()) {
            return category.get().getName();
        }
        return "Not Found";
    }

    @Transactional
    public Long createCategory(String name, Long userId) {
        logger.info("Creating category: {}", name);
        return categoryDao.createCategory(name, userId);
    }

    @Transactional
    public void toggleCategoryStatus(Long categoryId, Long userId) {
        logger.info("Toggling category status: {}", categoryId);
        categoryDao.toggleCategoryStatus(categoryId, userId);
    }

    @Transactional
    public void renameCategory(Long categoryId, String newName, Long userId){
        logger.info("Renaming category: {}", categoryId);
        categoryDao.renameCategory(categoryId, newName, userId);
    }
}
