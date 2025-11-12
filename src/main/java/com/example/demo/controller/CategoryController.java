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
            List<Category> categories = categoryService.getAllCategory();
            response.setBody(categories);
            response.setMessage("Fetched all categories successfully");
        } catch (Exception e) {
            log.error("Error fetching all categories", e);
            throw e; // để GlobalExceptionHandler xử lý
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
            Category category = categoryService.getCategory(id);
            response.setBody(category);
            response.setMessage("Fetched category successfully");
        } catch (Exception e) {
            log.error("Error fetching category with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Category> createCategory(@Valid @RequestBody Category category) {
        log.info("Request to create category: {}", category.getCategoryName());
        Category created = categoryService.createCategory(category);

        BaseResponse<Category> response = new BaseResponse<>();
        response.setMessage("Category created successfully");
        response.setBody(created);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Category> updateCategory(@PathVariable long id, @Valid @RequestBody Category category) {
        BaseResponse<Category> response = new BaseResponse<>();
        try {
            log.info("Request to update category with ID: {}", id);
            Category updated = categoryService.updateCategory(id, category);
            response.setBody(updated);
            response.setMessage("Category updated successfully");
        } catch (Exception e) {
            log.error("Error updating category with ID: {}", id, e);
            throw e;
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
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
