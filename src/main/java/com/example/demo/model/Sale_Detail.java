package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_detail", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sale_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private Long saleDetailId;

    @NotNull(message = "Sale ID cannot be null")
    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @NotNull(message = "Product ID cannot be null")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "Sale price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Sale price must have up to 10 integer digits and 2 decimal places")
    @Column(name = "sale_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal salePrice;
}
