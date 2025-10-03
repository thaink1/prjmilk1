package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.repo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductService {
    final ProductRepo productRepo;
    final BrandRepo brandRepo;
    final CategoryRepo categoryRepo;
    final DistributorRepo distributorRepo;
    final ObjectMapper objectMapper;
    final ImageRepo imageRepo;
    final PromotionRepo promotionRepo;

    @PersistenceContext
    EntityManager entityManager;

    public ProductResponse getProductById(long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductIF result = productRepo.findProductByIdWithRelations(product.getProductId());
        ProductResponse productResponse = objectMapper.convertValue(result, new TypeReference<ProductResponse>(){});
        List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(result.getProductId());
        List<ImageResponse> images = objectMapper
                .convertValue(imageIFs, new TypeReference<List<ImageResponse>>() {});
        productResponse.setImages(images);
        return  productResponse;
    }

    public List<ProductResponse> getAllProducts() {
        List<ProductIF> results = productRepo.findAllProductsWithRelations();
        List<ProductResponse> responses = objectMapper
                .convertValue(results, new TypeReference<List<ProductResponse>>() {});
        for (ProductResponse response : responses) {
            List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(response.getProductId());
            List<ImageResponse> images = objectMapper
                    .convertValue(imageIFs, new TypeReference<List<ImageResponse>>() {});
            response.setImages(images);
        }

        return responses;
    }

@Transactional
public ProductResponse addProduct(ProductRequest request) {
    Product product = new Product();

    if (!categoryRepo.existsById(request.getCategoryId())) {
        throw new IllegalArgumentException("Category id not found");
    }
    if (!brandRepo.existsById(request.getBrandId())) {
        throw new IllegalArgumentException("Brand not found");
    }
    if (!distributorRepo.existsById(request.getDistributorId())) {
        throw new IllegalArgumentException("Distributor not found");
    }
    if (request.getCreatedDate().after(request.getExpiredDate())) {
        throw new IllegalArgumentException("Created date cannot be after expired date");
    }

    BeanUtils.copyProperties(request, product);
    productRepo.save(product);

    if (request.getImages() != null) {
        long primaryCount = request.getImages().stream().filter(ImageRequest::getIsPrimary).count();
        if (primaryCount > 1) {
            throw new IllegalArgumentException("Only one primary image is supported");
        }
        for (var img : request.getImages()) {
            Image image = new Image();
            image.setProductId(product.getProductId());
            image.setUrl(img.getUrl());
            image.setIsPrimary(img.getIsPrimary());
            image.setCreatedAt(LocalDateTime.now());
            imageRepo.save(image);
        }
    }

    ProductIF productIF = productRepo.findProductByIdWithRelations(product.getProductId());
    ProductResponse response = objectMapper.convertValue(productIF, ProductResponse.class);

    List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(product.getProductId());
    List<ImageResponse> imageResponses =
            objectMapper.convertValue(imageIFs, new TypeReference<List<ImageResponse>>() {});
    response.setImages(imageResponses);

    return response;
}

@Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
    Product product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    if (!categoryRepo.existsById(request.getCategoryId())) {
        throw new IllegalArgumentException("Category id not found");
    }
    if (!brandRepo.existsById(request.getBrandId())) {
        throw new IllegalArgumentException("Brand not found");
    }
    if (!distributorRepo.existsById(request.getDistributorId())) {
        throw new IllegalArgumentException("Distributor not found");
    }
    if (request.getCreatedDate().after(request.getExpiredDate())) {
        throw new IllegalArgumentException("Created date cannot be after expired date");
    }
    BeanUtils.copyProperties(request, product);
    productRepo.save(product);
    if (request.getImages() != null) {
        imageRepo.deleteByProductId(product.getProductId());
        long primaryCount = request.getImages().stream().filter(ImageRequest::getIsPrimary).count();
        if (primaryCount > 1) {
            throw new IllegalArgumentException("Only one primary image is supported");
        }else if (primaryCount == 0 ) {
            throw new IllegalArgumentException("Must be one primary image");
        }
        for (var img : request.getImages()) {
            Image image = new Image();
            image.setProductId(product.getProductId());
            image.setUrl(img.getUrl());
            image.setIsPrimary(img.getIsPrimary());
            image.setCreatedAt(LocalDateTime.now());
            imageRepo.save(image);
        }
    }
    ProductIF result = productRepo.findProductByIdWithRelations(product.getProductId());
    ProductResponse productResponse = objectMapper.convertValue(result, new TypeReference<ProductResponse>(){});
    List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(result.getProductId());
    List<ImageResponse> images = objectMapper
            .convertValue(imageIFs, new TypeReference<List<ImageResponse>>() {});
    productResponse.setImages(images);
    return  productResponse;
    }

    public void deleteByProductId(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.deleteById(product.getProductId());
        imageRepo.deleteByProductId(product.getProductId());
        promotionRepo.deleteByProductId(product.getProductId());
    }

}
