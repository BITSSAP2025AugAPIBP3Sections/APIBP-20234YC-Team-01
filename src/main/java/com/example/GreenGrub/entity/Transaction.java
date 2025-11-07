package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.FoodType;
import com.example.GreenGrub.enumeration.UnitType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
/*
    A transaction is a completed exchange between two users on the platform, where one user (the donor) offers excess food and another user (the recipient) receives it.
    The transaction captures the details of the exchange.
 */
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transactionId", nullable = false, updatable = false, unique = true)
    private String transactionId;

    // Store only the foreign key value from Food table
    @Column(name = "food_id")
    private Long foodId;

    // Store only the foreign key value from FoodRequest table
    @Column(name = "food_request_id")
    private Long foodRequestId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    // Optional rating (1 to 5)
    @Column
    @PositiveOrZero
    @Max(5)
    private Integer rating;

    // Optional review text
    @Column(length = 500)
    private String review;



    // TBD in DB Layer: enforce that only one of foodId or foodRequestId can be non-null per transaction

}
