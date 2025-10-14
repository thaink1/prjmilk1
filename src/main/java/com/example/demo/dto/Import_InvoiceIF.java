package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Import_InvoiceIF {
    Long getImportId();
    LocalDate getDate();
    String getDistributorName();
    BigDecimal getTotalAmount();
}
