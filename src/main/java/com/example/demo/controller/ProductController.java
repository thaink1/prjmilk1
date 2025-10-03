package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    BaseResponse<List<ProductResponse>> getAllProducts() {
        BaseResponse<List<ProductResponse>> response = new BaseResponse<>();
        response.setBody(productService.getAllProducts());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @GetMapping("/{id}")
    BaseResponse<ProductResponse> getProductById(@PathVariable Long id) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(productService.getProductById(id));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    @PostMapping("")
    BaseResponse<ProductResponse> addProduct(@RequestBody @Valid ProductRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(productService.addProduct(request));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

   @PutMapping("/{id}")
   BaseResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setBody(productService.updateProduct(id, request));
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
   }

   @DeleteMapping("/{id}")
    BaseResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteByProductId(id);
        BaseResponse<Void> response = new BaseResponse<>();
        response.setMessage("Product deleted successfully");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
   }
}
