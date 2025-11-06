package com.example.GreenGrub.services;

import com.example.GreenGrub.dtos.DonationReportDTO;
import com.example.GreenGrub.dtos.MessageDTO;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.repositories.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    // Generate donation report
    public DonationReportDTO generateReport(String organizationId) {
        // TODO: Implement actual report logic
        DonationReportDTO report = new DonationReportDTO();
        report.setOrganizationId(organizationId);
//        report.setTotalDonations(42);
//        report.setTotalFoodDonated(100.0);
        return report;
    }

    // Send a chat message between donor and receiver
    public void sendMessage(MessageDTO message) {
//        System.out.println("Message sent: " + message.getMessage());
    }

    // Get donation history for a user though the Id
    public List<Donation> getDonationHistory(String userId) {
        return donationRepository.findByDonorIdOrRecipientId(userId, userId);
    }
}