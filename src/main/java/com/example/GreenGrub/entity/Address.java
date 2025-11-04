package com.example.GreenGrub.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Address {
    @Id
    @Column(name = "address_id", nullable = false, updatable = false, unique = true)
    private String addressId;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 150, message = "Address cannot exceed 150 characters")
    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 50, message = "City name cannot exceed 50 characters")
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @NotBlank(message = "State cannot be empty")
    @Size(max = 50, message = "State name cannot exceed 50 characters")
    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @NotBlank(message = "Pincode cannot be empty")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Indian pincode")
    @Column(name = "pincode", nullable = false, length = 6)
    private String pinCode;

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.0")
    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0")
    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;
}
