package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.model.Brand;
import com.example.demo.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/brand")
public class BrandController {
    BrandService brandService;
    ObjectMapper objectMapper;
    @GetMapping("")
    BaseResponse<List<Brand>> getAllBrands() {
        BaseResponse<List<Brand>> response = new BaseResponse<>();
        response.setBody(brandService.getAllBrands());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    BaseResponse<Brand> getBrand(@PathVariable long id) {
        BaseResponse<Brand> response = new BaseResponse<>();
        response.setBody(brandService.getBrand(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    BaseResponse<Brand> createBrand(@Valid  @RequestBody Brand brand) {
        BaseResponse<Brand> response = new BaseResponse<>();
        response.setBody(brandService.createBrand(brand));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    BaseResponse<Brand> updateBrand(@PathVariable Long id, @Valid @RequestBody Brand brand) {
        BaseResponse<Brand> response = new BaseResponse<>();
        response.setBody(brandService.updateBrand(id, brand));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    BaseResponse<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("delete success");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
