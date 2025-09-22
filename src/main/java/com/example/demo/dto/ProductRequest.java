package com.example.demo.dto;

import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ProductRequest {
    private String productName;
    private int capacity;
    private int weightGr;
    private float price;
    private int stockQuantity;
    private Date createdDate;
    private Date expiredDate;

    private Long categoryId;
    private Long brandId;
    private Long distributorId;

    private List<ImageRequest> images;
}
