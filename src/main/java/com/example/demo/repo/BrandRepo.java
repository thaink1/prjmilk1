package com.example.demo.repo;

import com.example.demo.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {
    boolean existsBrandByName(String name);
    Optional<Brand> findByBrandId(Long brandId);
}
