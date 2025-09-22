package com.example.demo.controller;

import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/brand")
public class BrandController {
    BrandService brandService;
    ObjectMapper objectMapper;
    @GetMapping("")
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }
    @GetMapping("/{id}")
    public Brand getBrand(@PathVariable long id) {
        return brandService.getBrand(id);
    }
    @PostMapping("")
    public Brand createBrand(@RequestBody Brand brand) {
        return brandService.createBrand(brand);
    }
    @PutMapping("/{id}")
    public Brand updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        return brandService.updateBrand(id, brand);
    }
    @DeleteMapping("/{id}")
    String deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return "Brand has been deleted!!" ;
    }
}
