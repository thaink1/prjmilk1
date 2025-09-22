package com.example.demo.repo;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    boolean existsCategoryByCategoryName(String name);
    Optional<Category> getCategoryById(Long id);
}
