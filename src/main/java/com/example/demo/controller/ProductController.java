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
        log.info("Fetching all products...");
        List<ProductResponse> products = productService.getAllProducts();

        BaseResponse<List<ProductResponse>> response = new BaseResponse<>();
        response.setBody(products);
        response.setMessage("Fetched all products successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("Fetching product with ID: {}", id);
        ProductResponse product = productService.getProductById(id);

        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(product);
        response.setMessage("Fetched product successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    public BaseResponse<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Creating new product: {}", request.getProductName());
        ProductResponse created = productService.addProduct(request);

        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(created);
        response.setMessage("Product created successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        ProductResponse updated = productService.updateProduct(id, request);

        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(updated);
        response.setMessage("Product updated successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with ID: {}", id);
        productService.deleteByProductId(id);

        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Product deleted successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
