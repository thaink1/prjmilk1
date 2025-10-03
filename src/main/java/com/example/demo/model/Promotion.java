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
@Table(name = "promotion", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 150, message = "Name must be between 3 and 150 characters")
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.01", message = "Value must be greater than 0")
    private BigDecimal value;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Column(name = "product_id", nullable = false)
    @NotNull(message = "Can not null in productId field")
    private Long productId;

}
