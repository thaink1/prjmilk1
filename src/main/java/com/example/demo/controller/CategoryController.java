package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public BaseResponse<List<Category>> getCategories() {
        BaseResponse<List<Category>> response = new BaseResponse<>();
        try {
            log.info("Request to get all categories");
            response.setBody(categoryService.getAllCategory());
            response.setMessage("Fetched all categories successfully");
        } catch (Exception e) {
            log.error("Error fetching all categories", e);
            response.setMessage("Failed to fetch categories: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Category> getCategory(@PathVariable long id) {
        BaseResponse<Category> response = new BaseResponse<>();
        try {
            log.info("Request to get category with ID: {}", id);
            response.setBody(categoryService.getCategory(id));
            response.setMessage("Fetched category successfully");
        } catch (Exception e) {
            log.error("Error fetching category with ID: {}", id, e);
            response.setMessage("Failed to fetch category: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Category> createCategory(@Valid @RequestBody Category category) {
        BaseResponse<Category> response = new BaseResponse<>();
        try {
            log.info("Request to create category: {}", category.getCategoryName());
            response.setBody(categoryService.createCategory(category));
            response.setMessage("Category created successfully");
        } catch (Exception e) {
            log.error("Error creating category: {}", category.getCategoryName(), e);
            response.setMessage("Failed to create category: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Category> updateCategory(@PathVariable long id, @Valid @RequestBody Category category) {
        BaseResponse<Category> response = new BaseResponse<>();
        try {
            log.info("Request to update category with ID: {}", id);
            response.setBody(categoryService.updateCategory(id, category));
            response.setMessage("Category updated successfully");
        } catch (Exception e) {
            log.error("Error updating category with ID: {}", id, e);
            response.setMessage("Failed to update category: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteCategory(@PathVariable long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete category with ID: {}", id);
            categoryService.deleteCategory(id);
            response.setMessage("Category deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting category with ID: {}", id, e);
            response.setMessage("Failed to delete category: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
