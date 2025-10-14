package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @NotNull(message = "Product ID cannot be null")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull(message = "Change quantity cannot be null")
    @Column(name = "change_quantity", nullable = false)
    private Integer changeQuantity;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date must be in the past or present")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 50, message = "Reason must not exceed 50 characters")
    @Column(name = "reason", length = 50)
    private String reason;
}
