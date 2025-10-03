package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionResponse {
    private Long promotionId;
    private String name;
    private BigDecimal value;
    private LocalDate startDate;
    private LocalDate endDate;
    private String productName;
}
