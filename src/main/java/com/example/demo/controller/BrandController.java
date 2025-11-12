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
            List<Brand> brands = brandService.getAllBrands();
            response.setBody(brands);
            response.setMessage("Fetched all brands successfully");
        } catch (Exception e) {
            log.error("Error fetching all brands", e);
            throw e; // Để GlobalExceptionHandler xử lý lỗi
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
            Brand brand = brandService.getBrand(id);
            response.setBody(brand);
            response.setMessage("Fetched brand successfully");
        } catch (Exception e) {
            log.error("Error fetching brand with ID: {}", id, e);
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<Brand> createBrand(@Valid @RequestBody Brand brand) {
        log.info("Request to create brand with name: {}", brand.getName());
        Brand created = brandService.createBrand(brand);

        BaseResponse<Brand> response = new BaseResponse<>();
        response.setMessage("Brand created successfully");
        response.setBody(created);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<Brand> updateBrand(@PathVariable Long id, @Valid @RequestBody Brand brand) {
        BaseResponse<Brand> response = new BaseResponse<>();
        try {
            log.info("Request to update brand with ID: {}", id);
            Brand updated = brandService.updateBrand(id, brand);
            response.setBody(updated);
            response.setMessage("Brand updated successfully");
        } catch (Exception e) {
            log.error("Error updating brand with ID: {}", id, e);
            throw e;
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
            throw e;
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
