package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale_DetailResponse {
    private long saleDetailId;
    private long saleId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
