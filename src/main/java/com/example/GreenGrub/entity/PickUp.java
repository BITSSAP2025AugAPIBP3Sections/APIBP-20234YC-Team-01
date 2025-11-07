package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.PickupStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickUp {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private String id;

    @NotNull(message = "Donation reference cannot be null")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", referencedColumnName = "donation_id", nullable = false)
    private Donation donation;

    @Column(name = "assigned_to")
    private String assignedToUserId;

    @NotBlank(message = "Pickup contact name cannot be empty")
    @Size(max = 100, message = "Contact name cannot exceed 100 characters")
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    @Column(name = "contact_number", nullable = false, length = 10)
    private String contactNumber;

    @NotNull(message = "Pickup address is required")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_address_id", referencedColumnName = "address_id", nullable = false)
    private Address pickupAddress;

    @FutureOrPresent(message = "Pickup date and time cannot be in the past")
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PickupStatus status;

    @Column(name = "remarks", length = 250)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
