package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size( max = 100, message = "Category name must not exceed 100 characters")
    @Size( min = 5, message = "Category name must be at least 5 characters")
    @Column(name = "name")
    private String categoryName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(name = "description")
    private String categoryDescription;
}
