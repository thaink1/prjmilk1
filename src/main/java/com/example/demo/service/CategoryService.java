package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repo.CategoryRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepo categoryRepo;
    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }
    public Category getCategory(long id){
        return categoryRepo.getCategoryById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
    public Category createCategory(Category category){
        if (categoryRepo.existsCategoryByCategoryName(category.getCategoryName())) {
            throw new RuntimeException("Category already exists");
        }
        return categoryRepo.save(category);
    }
    public Category updateCategory(Long id, Category category){
        Category oldCategory = categoryRepo.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (category.getCategoryName() != null && !category.getCategoryName().isEmpty()) {
            oldCategory.setCategoryName(category.getCategoryName());
        }
        if (category.getCategoryDescription() != null && !category.getCategoryDescription().isEmpty()) {
            oldCategory.setCategoryDescription(category.getCategoryDescription());
        }
        return categoryRepo.save(oldCategory);
    }
    public void deleteCategory(Long id){
        Category category = categoryRepo.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepo.deleteById(id);
    }


}
