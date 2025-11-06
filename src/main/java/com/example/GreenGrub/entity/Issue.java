package com.example.GreenGrub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "issues")
public class Issue {

    @Id
    private String id;

    // Store the foreign key value (no relationship)
    @Column(name = "food_id", nullable = false)
    private String foodId;

    @Column(name = "food_request_id", nullable = false)
    private String foodRequestId;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reportedAt;
}
