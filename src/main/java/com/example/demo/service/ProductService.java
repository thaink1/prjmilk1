package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.redis.RedisService;
import com.example.demo.repo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepo productRepo;
    BrandRepo brandRepo;
    CategoryRepo categoryRepo;
    DistributorRepo distributorRepo;
    ObjectMapper objectMapper;
    ImageRepo imageRepo;
    PromotionRepo promotionRepo;
    RedisService redisService;

    private static final String PRODUCT_CACHE = "product:";
    private static final String ALL_PRODUCTS_CACHE = "allProducts";

    public ProductResponse getProductById(long id) {
        String cacheKey = PRODUCT_CACHE + id;
        try {
            ProductResponse cached = redisService.getValue(cacheKey, ProductResponse.class);
            if (cached != null ) {
                log.info("product {} từ Redis cache", id);
                return cached;
            }

            log.info("Fetching product with ID: {}", id);
            Product product = productRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", id);
                        return new RuntimeException("Product not found");
                    });

            ProductIF result = productRepo.findProductByIdWithRelations(product.getProductId());
            ProductResponse response = objectMapper.convertValue(result, ProductResponse.class);

            List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(result.getProductId());
            List<ImageResponse> images = objectMapper.convertValue(imageIFs, new TypeReference<>() {});
            response.setImages(images);

            redisService.setValue(cacheKey, response, Duration.ofMinutes(30));
            log.info("Đã cache product {} vào Redis", id);
            return response;
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", id, e);
            throw e;
        }
    }

    public List<ProductResponse> getAllProducts() {
        try {
            List<ProductResponse> cached = redisService.getValue(ALL_PRODUCTS_CACHE, List.class);
            if (cached != null && !cached.isEmpty()) {
                log.info("Lấy danh sách sản phẩm từ Redis cache");
                return cached;
            }

            log.info("Fetching all products...");
            List<ProductIF> results = productRepo.findAllProductsWithRelations();
            List<ProductResponse> responses = objectMapper.convertValue(results, new TypeReference<>() {});
            for (ProductResponse response : responses) {
                List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(response.getProductId());
                List<ImageResponse> images = objectMapper.convertValue(imageIFs, new TypeReference<>() {});
                response.setImages(images);
            }

            redisService.setValue(ALL_PRODUCTS_CACHE, responses, Duration.ofMinutes(15));
            log.info("Đã cache danh sách sản phẩm vào Redis ({} items)", responses.size());

            return responses;
        } catch (Exception e) {
            log.error("Error fetching all products", e);
            throw e;
        }
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        try {
            log.info("Creating new product with name: {}", request.getProductName());
            validateProductRequest(request);

            Product product = new Product();
            BeanUtils.copyProperties(request, product);
            productRepo.save(product);

            log.info("Product created successfully with ID: {}", product.getProductId());

            // Lưu ảnh
            if (request.getImages() != null) {
                validatePrimaryImage(request);
                for (var img : request.getImages()) {
                    Image image = new Image();
                    image.setProductId(product.getProductId());
                    image.setUrl(img.getUrl());
                    image.setIsPrimary(img.getIsPrimary());
                    image.setCreatedAt(LocalDateTime.now());
                    imageRepo.save(image);
                }
                log.info("Saved {} images for product {}", request.getImages().size(), product.getProductId());
            }

            ProductIF productIF = productRepo.findProductByIdWithRelations(product.getProductId());
            ProductResponse response = objectMapper.convertValue(productIF, ProductResponse.class);
            List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(product.getProductId());
            List<ImageResponse> imageResponses = objectMapper.convertValue(imageIFs, new TypeReference<>() {});
            response.setImages(imageResponses);

            redisService.deleteValue(ALL_PRODUCTS_CACHE);

            log.info("Product created successfully and cache invalidated");
            return response;
        } catch (Exception e) {
            log.error("Error creating product: {}", request.getProductName(), e);
            throw e;
        }
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        String cacheKey = PRODUCT_CACHE + id;
        try {
            log.info("Updating product with ID: {}", id);
            Product product = productRepo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", id);
                        return new RuntimeException("Product not found");
                    });

            validateProductRequest(request);
            BeanUtils.copyProperties(request, product);
            productRepo.save(product);
            log.info("Product base info updated successfully with ID: {}", id);

            if (request.getImages() != null) {
                imageRepo.deleteByProductId(product.getProductId());
                validatePrimaryImage(request);
                for (var img : request.getImages()) {
                    Image image = new Image();
                    image.setProductId(product.getProductId());
                    image.setUrl(img.getUrl());
                    image.setIsPrimary(img.getIsPrimary());
                    image.setCreatedAt(LocalDateTime.now());
                    imageRepo.save(image);
                }
                log.info("Updated {} images for product {}", request.getImages().size(), product.getProductId());
            }

            ProductIF result = productRepo.findProductByIdWithRelations(product.getProductId());
            ProductResponse response = objectMapper.convertValue(result, new TypeReference<>() {});
            List<ProductImageIF> imageIFs = productRepo.findImagesByProductId(result.getProductId());
            List<ImageResponse> images = objectMapper.convertValue(imageIFs, new TypeReference<>() {});
            response.setImages(images);

            redisService.deleteValue(cacheKey);
            redisService.deleteValue(ALL_PRODUCTS_CACHE);
            log.info("Invalidated cache after updating product {}", id);

            return response;
        } catch (Exception e) {
            log.error("Error updating product with ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public void deleteByProductId(Long productId) {
        String cacheKey = PRODUCT_CACHE + productId;
        try {
            log.info("Deleting product with ID: {}", productId);
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", productId);
                        return new RuntimeException("Product not found");
                    });

            imageRepo.deleteByProductId(productId);
            promotionRepo.deleteByProductId(productId);
            productRepo.deleteById(productId);

            redisService.deleteValue(cacheKey);
            redisService.deleteValue(ALL_PRODUCTS_CACHE);

            log.info("Deleted product {} and cleared Redis cache", productId);
        } catch (Exception e) {
            log.error("Error deleting product with ID: {}", productId, e);
            throw e;
        }
    }

    private void validateProductRequest(ProductRequest request) {
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
    }

    private void validatePrimaryImage(ProductRequest request) {
        long primaryCount = request.getImages().stream().filter(ImageRequest::getIsPrimary).count();
        if (primaryCount > 1) {
            throw new IllegalArgumentException("Only one primary image is supported");
        } else if (primaryCount == 0) {
            throw new IllegalArgumentException("Must have one primary image");
        }
    }
}
