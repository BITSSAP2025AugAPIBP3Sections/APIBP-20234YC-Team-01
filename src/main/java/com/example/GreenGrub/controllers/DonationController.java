package com.example.GreenGrub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.GreenGrub.services.DonationService;
import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.entity.Donation;

import java.util.List;

@RestController
@RequestMapping("/donations")
@Tag(name = "Donation Management", description = "Endpoints for donation report, history and contacts of Donors and Recipients")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // Generate Donation Reports
    @GetMapping("/report")
    @Operation(summary = "Fetch Donation Report", description = "This is to fetch a detailed report of donation")
    public DonationReportDTO generateReport(@RequestParam(required = false) String organizationId) {
        return donationService.generateReport(organizationId);
    }

    // Contact Donor or Receiver
    @PostMapping("/contact")
    @Operation(summary = "Contact Donor/Recipient", description = "Contact Donor or Recipient")
    public String sendMessage(@RequestBody MessageDTO message) {
        donationService.sendMessage(message);
        return "Message sent";
    }

    // View Donation History
    @GetMapping("/history/{userId}")
    @Operation(summary = "Fetch Donation history", description = "Fetch the history of donation by a donor")
    public List<Donation> getDonationHistory(@PathVariable String userId) {
        return donationService.getDonationHistory(userId);
    }
}