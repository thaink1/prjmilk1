package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public BaseResponse<List<ProductResponse>> getAllProducts() {
        BaseResponse<List<ProductResponse>> response = new BaseResponse<>();
        try {
            log.info("Fetching all products...");
            response.setBody(productService.getAllProducts());
            response.setMessage("Fetched all products successfully");
        } catch (Exception e) {
            log.error("Error fetching all products", e);
            response.setMessage("Failed to fetch products: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<ProductResponse> getProductById(@PathVariable Long id) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        try {
            log.info("Fetching product with ID: {}", id);
            response.setBody(productService.getProductById(id));
            response.setMessage("Fetched product successfully");
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", id, e);
            response.setMessage("Failed to fetch product: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        try {
            log.info("Creating new product: {}", request.getProductName());
            response.setBody(productService.addProduct(request));
            response.setMessage("Product created successfully");
        } catch (Exception e) {
            log.error("Error creating product: {}", request.getProductName(), e);
            response.setMessage("Failed to create product: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        try {
            log.info("Updating product with ID: {}", id);
            response.setBody(productService.updateProduct(id, request));
            response.setMessage("Product updated successfully");
        } catch (Exception e) {
            log.error("Error updating product with ID: {}", id, e);
            response.setMessage("Failed to update product: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProduct(@PathVariable Long id) {
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            log.info("Deleting product with ID: {}", id);
            productService.deleteByProductId(id);
            response.setMessage("Product deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting product with ID: {}", id, e);
            response.setMessage("Failed to delete product: " + e.getMessage());
        }
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
