package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "product", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String productName;

    @Column(name = "capacity_ml")
    private int capacity;

    @Column(name = "weight_gr")
    private int weightGr;

    @Column(name = "unit_price")
    private float price;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "mfg_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "exp_date")
    @Temporal(TemporalType.DATE)
    private Date expiredDate;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "distributor_id")
    private Long distributorId;
}
