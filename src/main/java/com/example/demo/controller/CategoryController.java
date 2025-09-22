package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    CategoryService categoryService;
    ObjectMapper objectMapper;
    @GetMapping("")
    public List<Category> getCategories() {
        return categoryService.getAllCategory();
    }
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable long id) {
        return categoryService.getCategory(id);
    }
    @PostMapping("")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    @DeleteMapping("/{id}")
    String deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return "Category has been deleted";
    }
}
