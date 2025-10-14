package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Sale_InvoiceResponse {
    private long saleId;
    private LocalDate date;
    String customerName;
    BigDecimal totalAmount;
}
