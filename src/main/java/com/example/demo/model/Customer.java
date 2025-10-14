package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Customer name must not be blank")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Phone number must start with 0 and contain exactly 10 digits"
    )
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @NotBlank(message = "Email must not be blank")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$",
            message = "Email must be valid abc@example.com"
    )
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", length = 255)
    private String address;

    @PastOrPresent(message = "Created date cannot be in the future")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
