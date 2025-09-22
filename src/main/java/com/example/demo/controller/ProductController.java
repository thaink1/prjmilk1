package com.example.demo.controller;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("")
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }
}
