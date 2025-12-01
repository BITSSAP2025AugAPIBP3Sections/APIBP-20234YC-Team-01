package com.example.GreenGrub.entity;

import com.example.GreenGrub.enumeration.DonationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "donation_id")
    private String donationId;

    @ElementCollection
    @CollectionTable(name = "donation_foods", joinColumns = @JoinColumn(name = "donation_id"))
    @Column(name = "food_id")
    private List<String> foodIdList;

    @Getter
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "requested", nullable = false)
    private boolean requested;

    @Column(name = "donor_id", nullable = false)
    private String donorId;

    @Getter
    @Column(name = "recipient_id")
    private String recipientId;

    @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid website URL")
    @Column(name = "website_url")
    private String websiteUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_address_id",referencedColumnName = "address_id")
    @Valid
    private Address pickupAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address_id",referencedColumnName = "address_id")
    @Valid
    private Address deliveryAddress;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DonationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
