package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PromotionIF {
    Long getPromotionId();
    String getName();
    BigDecimal getValue();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getProductName();
}
