package com.example.demo.dto;

import java.math.BigDecimal;

public interface Import_DetailIF {
    long getIpDetailId();
    long getImportId();
    String getProductName();
    int getQuantity();
    BigDecimal getPrice();
}
