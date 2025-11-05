package com.example.GreenGrub.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.GreenGrub.services.DonationService;
import com.example.GreenGrub.dtos.DonationReportDTO;
import com.example.GreenGrub.dtos.MessageDTO;
import com.example.GreenGrub.entity.Donation;

import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // Generate Donation Reports
    @GetMapping("/report")
    public DonationReportDTO generateReport(@RequestParam(required = false) String organizationId) {
        return donationService.generateReport(organizationId);
    }

    // Contact Donor or Receiver
    @PostMapping("/contact")
    public String sendMessage(@RequestBody MessageDTO message) {
        donationService.sendMessage(message);
        return "Message sent";
    }

    // View Donation History
    @GetMapping("/history")
    public List<Donation> getDonationHistory(@RequestParam String userId) {
        return donationService.getDonationHistory(userId);
    }
}