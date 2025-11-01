package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
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
@RequestMapping("/brand")
@Slf4j
public class BrandController {

    private final BrandService brandService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public BaseResponse<List<Brand>> getAllBrands() {
        BaseResponse<List<Brand>> response = new BaseResponse<>();
        try {
            log.info("Request to get all brands");
            response.setBody(brandService.getAllBrands());
            response.setMessage("Fetched all brands successfully");
        } catch (Exception e) {
            log.error("Error fetching all brands", e);
            response.setMessage("Failed to fetch brands: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<Brand> getBrand(@PathVariable long id) {
        BaseResponse<Brand> response = new BaseResponse<>();
        try {
            log.info("Request to get brand with ID: {}", id);
            response.setBody(brandService.getBrand(id));
            response.setMessage("Fetched brand successfully");
        } catch (Exception e) {
            log.error("Error fetching brand with ID: {}", id, e);
            response.setMessage("Failed to fetch brand: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Brand> createBrand(@Valid @RequestBody Brand brand) {
        BaseResponse<Brand> response = new BaseResponse<>();
        try {
            log.info("Request to create brand with name: {}", brand.getName());
            response.setBody(brandService.createBrand(brand));
            response.setMessage("Brand created successfully");
        } catch (Exception e) {
            log.error("Error creating brand: {}", brand.getName(), e);
            response.setMessage("Failed to create brand: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Brand> updateBrand(@PathVariable Long id, @Valid @RequestBody Brand brand) {
        BaseResponse<Brand> response = new BaseResponse<>();
        try {
            log.info("Request to update brand with ID: {}", id);
            response.setBody(brandService.updateBrand(id, brand));
            response.setMessage("Brand updated successfully");
        } catch (Exception e) {
            log.error("Error updating brand with ID: {}", id, e);
            response.setMessage("Failed to update brand: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteBrand(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Request to delete brand with ID: {}", id);
            brandService.deleteBrand(id);
            response.setMessage("Brand deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting brand with ID: {}", id, e);
            response.setMessage("Failed to delete brand: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
