package com.example.demo.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ProductResponse {
    private Long productId;
    private String productName;
    private int capacity;
    private int weightGr;
    private float price;
    private float finalPrice;
    private int stockQuantity;
    private Date createdDate;
    private Date expiredDate;

    private String categoryName;
    private String brandName;
    private String distributorName;

    private List<ImageResponse> images;
}
