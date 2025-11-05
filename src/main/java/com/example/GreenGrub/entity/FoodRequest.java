package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.FoodType;
import com.example.GreenGrub.enumeration.UnitType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Represents a food request")

public class FoodRequest {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Schema(description = "Unique identifier for the food request.")
    private String foodRequestId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", nullable = false, length = 500)
    @Schema(description = "Description of the food request")
    private String description;

    @NotNull(message = "Quantity requested cannot be null")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(name = "quantity_requested", nullable = false)
    @Schema(description = "Quantity of food requested")
    private Integer quantityRequested;

    @NotNull(message = "Quantity Unit cannot be null")
    @Column(name = "unit_type",nullable = false)
    @Schema(description = "Unit type for the quantity", example = "KILOGRAMS, SERVINGS")
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    @Schema(description = "Type of food requested", example = "VEGETARIAN, NON_VEGETARIAN, JAIN")
    private FoodType foodType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requireOn;
}
