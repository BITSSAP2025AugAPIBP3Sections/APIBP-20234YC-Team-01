package com.example.GreenGrub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationReportDTO {

    private String organizationId;
    private int totalDonations;
    private double totalFoodDonated;

    public String getOrganizationId()
    {
        return organizationId;
    }

    public int getTotalDonations()
    {
        return totalDonations;
    }

    public void setTotalDonations(int totalDonations)
    {
        this.totalDonations = totalDonations;
    }

    public double getTotalFoodDonated()
    {
        return totalFoodDonated;
    }

    public void setTotalFoodDonated(double totalFoodDonated)
    {
        this.totalFoodDonated = totalFoodDonated;
    }

    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

}