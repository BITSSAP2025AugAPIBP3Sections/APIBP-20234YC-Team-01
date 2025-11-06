package com.example.GreenGrub.dtos;

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