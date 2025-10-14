package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Sale_InvoiceIF {
    long getSaleId();
    LocalDate getDate();
    String getCustomerName();
    BigDecimal getTotalAmount();
}
