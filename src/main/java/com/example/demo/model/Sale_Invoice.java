package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sale_invoice", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale_Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @NotNull(message = "Sale date cannot be null")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "Customer ID cannot be null")
    private long customerId;

    @NotNull(message = "Total amount cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Total amount must have up to 10 integer digits and 2 decimal places")
    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.valueOf(0.00);

}
