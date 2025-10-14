package com.example.demo.dto;

import java.math.BigDecimal;

public interface Sale_DetailIF {
    long getSaleDetailId();
    long getSaleId();
    String getProductName();
    int getQuantity();
    BigDecimal getPrice();
}
