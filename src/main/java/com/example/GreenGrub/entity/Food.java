package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.FoodType;
import com.example.GreenGrub.enumeration.UnitType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "food_items")

// Represents an excess food post

public class Food {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private String id;

    @NotBlank(message = "Food name cannot be empty")
    @Size(min = 2, max = 100, message = "Food name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", nullable = false, length = 500)
    private String description;

//    @NotNull(message = "Price cannot be null")
//    @Positive(message = "Price must be greater than 0")
//    @Column(name = "price", nullable = false)
//    private Double price;

    @NotNull(message = "Quantity available cannot be null")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @NotNull(message = "Quantity Unit cannot be null")
    @Column(name = "unit_type",nullable = false)
    private UnitType unitType;

    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid image URL")
    @Column(name = "image_url")
    private String imageUrl;

//    @Column(name = "is_available", nullable = false)
//    private boolean isAvailable = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

}

