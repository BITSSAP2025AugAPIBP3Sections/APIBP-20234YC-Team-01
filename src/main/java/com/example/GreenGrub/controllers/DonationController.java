package com.example.GreenGrub.controllers;

import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.services.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@Tag(name = "Donation Management", description = "Operations related to donation reports, history, and donor/recipient communication")
public class DonationController {

    @Autowired
    private DonationService donationService;

    /**
     * Fetch summary report of donations made by a specific donor.
     * If no userId is provided, returns a summary of all donations.
     */
    @GetMapping("/report")
    @Operation(
        summary = "Fetch Donation Report",
        description = "Returns a summary of donation statistics such as total donations and total food quantity donated."
    )
    public DonationReportDTO generateReport(@RequestParam(required = false) String userID) {
        return donationService.generateReport(userID);
    }

    /**
     * Download donation report as a CSV file.
     * If userId is not provided, generates a CSV with data for all donors.
     */
    @GetMapping(value = "/report/download", produces = "text/csv")
    @Operation(
        summary = "Download Donation Report (CSV)",
        description = "Generates a CSV summary of donations. Use userID to generate a donor-specific report."
    )
    public ResponseEntity<ByteArrayResource> downloadReportCsv(
        @RequestParam(required = false) String userID) throws Exception {

        byte[] csvBytes = donationService.generateCsvReportBytes(userID);
        ByteArrayResource resource = new ByteArrayResource(csvBytes);

        String date = LocalDate.now().toString();
        String userPart = (userID == null || userID.trim().isEmpty()) ? "ALL" : userID;
        String filename = "donation-report-" + date + "-" + userPart + ".csv";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .contentLength(csvBytes.length)
            .body(resource);
    }

    /**
     * Allows donor or recipient to exchange contact details based on donation status.
     * This returns a clear message explaining what contact information is shared.
     */
    @PostMapping("/contact")
    @Operation(
        summary = "Contact Donor or Recipient",
        description = "Initiates contact sharing between donor and recipient based on the donation status."
    )
    public String shareContacts(@RequestBody MessageDTO message) {
        return donationService.shareContacts(message);
    }

    /**
     * Fetch full donation history for a donor.
     */
    @GetMapping("/history/{userId}")
    @Operation(
        summary = "Fetch Donation History",
        description = "Returns the complete list of donations made by the given donor."
    )
    public List<Donation> getDonationHistory(@PathVariable String userId) {
        return donationService.getDonationHistory(userId);
    }
}
