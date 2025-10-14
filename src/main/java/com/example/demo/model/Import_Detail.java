package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "import_detail", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Import_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_detail_id")
    private Long ipDetailId;

    @NotNull(message = "Import ID must not be null")
    @Positive(message = "Import ID must be positive")
    @Column(name = "import_id")
    private Long importId;

    @NotNull(message = "Product ID must not be null")
    @Positive(message = "Product ID must be positive")
    @Column(name = "product_id")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private int quantity;

    @NotNull(message = "Import price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Import price must be greater than 0")
    @Column(name = "import_price", precision = 12, scale = 2)
    private BigDecimal price;
}
