package com.example.demo.dto;

import java.util.Date;

public interface ProductIF {
    Long getProductId();
    String getProductName();
    int getCapacity();
    int getWeightGr();
    float getPrice();
    float getFinalPrice();
    int getStockQuantity();
    Date getCreatedDate();
    Date getExpiredDate();
    String getCategoryName();
    String getBrandName();
    String getDistributorName();
}
