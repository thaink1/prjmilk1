package com.example.demo.service;

import com.example.demo.model.Brand;
import com.example.demo.repo.AccountRepo;
import com.example.demo.repo.BrandRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {
    private BrandRepo brandRepo;
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }
    public Brand getBrand(long id) {
        return brandRepo.findByBrandId(id).orElseThrow(() -> new RuntimeException("Brand not found"));
    }
    public Brand createBrand(Brand brand) {
        if (brandRepo.existsBrandByName(brand.getName())) {
            throw new RuntimeException("Brand with name " + brand.getName() + " already exists");
        }
        return brandRepo.save(brand);
    }
    public Brand updateBrand(long id, Brand brand) {
        Brand oldBrand = brandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
            oldBrand.setName(brand.getName());
            oldBrand.setDescription(brand.getDescription());
            oldBrand.setCountry(brand.getCountry());
        return brandRepo.save(oldBrand);
    }
    public void deleteBrand(long id) {
        Brand brand = brandRepo.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
        brandRepo.deleteById(id);
    }
}
