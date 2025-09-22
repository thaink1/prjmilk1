package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;
    private String name;
    private String description;
    private String country;
}
