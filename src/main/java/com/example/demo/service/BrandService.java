package com.example.demo.service;

import com.example.demo.model.Brand;
import com.example.demo.repo.BrandRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BrandService {
    private final BrandRepo brandRepo;
    public List<Brand> getAllBrands() {
        try {
            log.info("Fetching all brands...");
            List<Brand> brands = brandRepo.findAll();
            log.info("Fetched {} brands successfully", brands.size());
            return brands;
        } catch (Exception e) {
            log.error("Error while fetching all brands", e);
            throw e;
        }
    }
    public Brand getBrand(long id) {
        try {
            log.info("Fetching brand with ID: {}", id);
            Brand brand = brandRepo.findByBrandId(id)
                    .orElseThrow(() -> {
                        log.warn("Brand not found with ID: {}", id);
                        return new RuntimeException("Brand not found");
                    });
            log.info("Brand fetched successfully: {}", brand.getName());
            return brand;
        } catch (Exception e) {
            log.error("Error while fetching brand with ID: {}", id, e);
            throw e;
        }
    }

    public Brand createBrand(Brand brand) {
        try {
            log.info("Creating new brand: {}", brand.getName());
            if (brandRepo.existsBrandByName(brand.getName())) {
                log.warn("Brand with name '{}' already exists", brand.getName());
                throw new RuntimeException("Brand with name " + brand.getName() + " already exists");
            }
            Brand saved = brandRepo.save(brand);
            log.info("Brand created successfully: {}", saved.getName());
            return saved;
        } catch (Exception e) {
            log.error("Error while creating brand: {}", brand.getName(), e);
            throw e;
        }
    }

    public Brand updateBrand(long id, Brand brand) {
        try {
            log.info("Updating brand with ID: {}", id);
            Brand existing = brandRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Brand not found with ID: {}", id);
                        return new RuntimeException("Brand not found");
                    });

            existing.setName(brand.getName());
            existing.setDescription(brand.getDescription());
            existing.setCountry(brand.getCountry());

            Brand updated = brandRepo.save(existing);
            log.info("Brand updated successfully: {}", updated.getName());
            return updated;
        } catch (Exception e) {
            log.error("Error while updating brand with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteBrand(long id) {
        try {
            log.info("Deleting brand with ID: {}", id);
            Brand brand = brandRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Brand not found with ID: {}", id);
                        return new RuntimeException("Brand not found");
                    });
            brandRepo.deleteById(id);
            log.info("Brand deleted successfully: {}", brand.getName());
        } catch (Exception e) {
            log.error("Error while deleting brand with ID: {}", id, e);
            throw e;
        }
    }
}
