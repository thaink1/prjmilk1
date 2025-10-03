package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String productName;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than 0")
    private Integer weightGr;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Float price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 1, message = "Stock quantity must be >= 1")
    private Integer stockQuantity;

    @NotNull(message = "Created date is required")
    @PastOrPresent(message = "Created date cannot be in the future")
    private Date createdDate;

    @NotNull(message = "Expire date is required")
    @FutureOrPresent(message = "Expire date must be today or in the future")
    private Date expiredDate;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Brand is required")
    private Long brandId;

    @NotNull(message = "Distributor is required")
    private Long distributorId;

    private List<ImageRequest> images;
}
