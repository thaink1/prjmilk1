package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "import_invoice", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Import_Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long importId;

    @NotNull(message = "Import date must not be null")
    @PastOrPresent(message = "Import date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @NotNull(message = "Distributor ID must not be null")
    @Positive(message = "Distributor ID must be a positive number")
    @Column(name = "distributor_id")
    private Long distributorId;

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be >= 0")
    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;
}
