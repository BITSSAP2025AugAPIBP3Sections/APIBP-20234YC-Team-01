package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.FoodType;
import com.example.GreenGrub.enumeration.UnitType;
import jakarta.persistence.*;
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
@Table(name = "food_requests")

// Represents a food request post

public class FoodRequest {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private String foodRequestId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull(message = "Quantity requested cannot be null")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(name = "quantity_requested", nullable = false)
    private Integer quantityRequested;

    @NotNull(message = "Quantity Unit cannot be null")
    @Column(name = "unit_type",nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requireOn;
}
