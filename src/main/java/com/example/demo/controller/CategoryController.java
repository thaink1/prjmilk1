package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    CategoryService categoryService;
    ObjectMapper objectMapper;

    @GetMapping("")
     BaseResponse<List<Category>> getCategories() {
        BaseResponse <List<Category>> response = new BaseResponse<>();
        response.setBody(categoryService.getAllCategory());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    BaseResponse<Category> getCategory(@PathVariable long id) {
        BaseResponse<Category> response = new BaseResponse<>();
        response.setBody(categoryService.getCategory(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    BaseResponse<Category> createCategory(@Valid @RequestBody Category category) {
        BaseResponse<Category> response = new BaseResponse<>();
        response.setBody(categoryService.createCategory(category));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
    
    @PutMapping("/{id}")
    BaseResponse<Category> updateCategory(@PathVariable long id, @Valid @RequestBody Category category) {
        BaseResponse<Category> response = new BaseResponse<>();
        response.setBody(categoryService.updateCategory(id, category));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    BaseResponse<Void> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Delete Category successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
