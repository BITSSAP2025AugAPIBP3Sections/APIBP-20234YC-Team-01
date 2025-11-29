package com.example.GreenGrub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonationReportDTO {

    private String organizationId;
    private int totalDonations;
    private double totalFoodDonated;
}