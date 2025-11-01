package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repo.CategoryRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<Category> getAllCategory() {
        try {
            log.info("Fetching all categories...");
            List<Category> categories = categoryRepo.findAll();
            log.info("Fetched {} categories successfully", categories.size());
            return categories;
        } catch (Exception e) {
            log.error("Error while fetching all categories", e);
            throw e;
        }
    }

    public Category getCategory(long id) {
        try {
            log.info("Fetching category with ID: {}", id);
            Category category = categoryRepo.getCategoryById(id)
                    .orElseThrow(() -> {
                        log.warn("Category not found with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });
            log.info("Fetched category successfully: {}", category.getCategoryName());
            return category;
        } catch (Exception e) {
            log.error("Error while fetching category with ID: {}", id, e);
            throw e;
        }
    }

    public Category createCategory(Category category) {
        try {
            log.info("Creating new category: {}", category.getCategoryName());
            if (categoryRepo.existsCategoryByCategoryName(category.getCategoryName())) {
                log.warn("Category with name '{}' already exists", category.getCategoryName());
                throw new RuntimeException("Category already exists");
            }
            Category saved = categoryRepo.save(category);
            log.info("Category created successfully: {}", saved.getCategoryName());
            return saved;
        } catch (Exception e) {
            log.error("Error while creating category: {}", category.getCategoryName(), e);
            throw e;
        }
    }

    public Category updateCategory(Long id, Category category) {
        try {
            log.info("Updating category with ID: {}", id);
            Category oldCategory = categoryRepo.getCategoryById(id)
                    .orElseThrow(() -> {
                        log.warn("Category not found with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });

            oldCategory.setCategoryName(category.getCategoryName());
            oldCategory.setCategoryDescription(category.getCategoryDescription());

            Category updated = categoryRepo.save(oldCategory);
            log.info("Category updated successfully: {}", updated.getCategoryName());
            return updated;
        } catch (Exception e) {
            log.error("Error while updating category with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteCategory(Long id) {
        try {
            log.info("Deleting category with ID: {}", id);
            Category category = categoryRepo.getCategoryById(id)
                    .orElseThrow(() -> {
                        log.warn("Category not found with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });
            categoryRepo.deleteById(id);
            log.info("Category deleted successfully: {}", category.getCategoryName());
        } catch (Exception e) {
            log.error("Error while deleting category with ID: {}", id, e);
            throw e;
        }
    }
}
