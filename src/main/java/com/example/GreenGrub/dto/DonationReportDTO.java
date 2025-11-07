package com.example.GreenGrub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationReportDTO {
    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    private String organizationId;
    private int totalDonations;
    private double totalFoodDonated;
}