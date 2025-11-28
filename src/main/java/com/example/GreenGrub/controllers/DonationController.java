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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/donations")
@Tag(name = "Donation Management", description = "Operations related to donation reports, history, and donor/recipient communication")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // ---------------------------------------------------------
    // 1. Fetch summary report of donations (optionally by user)
    // ---------------------------------------------------------
    @GetMapping("/report")
    @Operation(
        summary = "Fetch Donation Report",
        description = "Returns a summary of donation statistics such as total donations and total food quantity donated. Access by [ADMIN, DONOR]"
    )
    public ResponseEntity<?> generateReport(@RequestParam(required = false) String userID) {
        DonationReportDTO report = donationService.generateReport(userID);

        if (report == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "code", 404,
                    "message", "No donation report found",
                    "suggestions", List.of("Verify user ID", "Ensure donations exist for this user"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        return ResponseEntity.ok(report);
    }

    // --------------------------------------------------------------
    // 2. Download donation report as CSV (optionally filtered by user)
    // --------------------------------------------------------------
    @GetMapping(value = "/report/download", produces = "text/csv")
    @Operation(
        summary = "Download Donation Report (CSV)",
        description = "Generates a CSV summary of donations. Use userID to generate a donor-specific report. Access by [ADMIN, DONOR]"
    )
    public ResponseEntity<?> downloadReportCsv(
        @RequestParam(required = false) String userID) throws Exception {

        byte[] csvBytes = donationService.generateCsvReportBytes(userID);

        if (csvBytes == null || csvBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "code", 404,
                    "message", "No donation data available to generate CSV",
                    "suggestions", List.of("Verify user ID", "Create some donations first"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

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

    // ---------------------------------------------------
    // 3. Share contacts between donor and recipient
    // ---------------------------------------------------
    @PostMapping("/contact")
    @Operation(
        summary = "Contact Donor or Recipient",
        description = "Initiates contact sharing between donor and recipient based on the donation status. Access by [DONOR, RECIPIENT]"
    )
    public ResponseEntity<Map<String, Object>> shareContacts(@RequestBody MessageDTO message) {
        String result = donationService.shareContacts(message);

        Map<String, Object> body = new HashMap<>();
        body.put("message", result);
        body.put("timeStamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(body);
    }

    // ---------------------------------------------------
    // 4. Fetch full donation history for a donor
    // ---------------------------------------------------
    @GetMapping("/history/{userId}")
    @Operation(
        summary = "Fetch Donation History",
        description = "Returns the complete list of donations made by the given donor. Access by [DONOR]"
    )
    public ResponseEntity<?> getDonationHistory(@PathVariable String userId) {
        List<Donation> history = donationService.getDonationHistory(userId);

        if (history == null || history.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "code", 404,
                    "message", "No donation history found for user: " + userId,
                    "suggestions", List.of("Verify user ID", "Make a new donation"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        return ResponseEntity.ok(history);
    }

    // ---------------------------------------------------
    // 5. Create donation
    // ---------------------------------------------------
    @PostMapping
    @Operation(
        summary = "Create a new donation",
        description = "Creates a new donation entry. Access by [DONOR]"
    )
    public ResponseEntity<?> createDonation(@RequestBody Donation donation) {
        if (donation == null) {
            return ResponseEntity.badRequest().body(
                Map.of(
                    "code", 400,
                    "message", "Donation payload cannot be null",
                    "suggestions", List.of("Provide donation details in request body"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        Donation created = donationService.createDonation(donation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------------------------------------
    // 6. Get donation by ID
    // ---------------------------------------------------
    @GetMapping("/{donationId}")
    @Operation(
        summary = "Get donation details",
        description = "Fetches a single donation by its ID. Access by [ADMIN, DONOR, RECIPIENT]"
    )
    public ResponseEntity<?> getDonationById(@PathVariable String donationId) {
        return donationService.getDonationById(donationId)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                        "code", 404,
                        "message", "Donation not found for ID: " + donationId,
                        "suggestions", List.of("Verify donation ID", "Create a new donation"),
                        "timeStamp", LocalDateTime.now().toString()
                    )
                )
            );
    }

    // ---------------------------------------------------
    // 7. List donations with filters
    // ---------------------------------------------------
    @GetMapping
    @Operation(
        summary = "List donations",
        description = "Lists all donations with optional filters by donor, recipient, or status. Access by [ADMIN, DONOR, RECIPIENT]"
    )
    public ResponseEntity<?> listDonations(
        @RequestParam(required = false) String donorId,
        @RequestParam(required = false) String recipientId,
        @RequestParam(required = false) String status   // e.g. PENDING/COMPLETED
    ) {
        List<Donation> donations = donationService.listDonations(donorId, recipientId, status);

        if (donations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "code", 404,
                    "message", "No donations found for the given filters",
                    "filters", Map.of(
                        "donorId", donorId,
                        "recipientId", recipientId,
                        "status", status
                    ),
                    "suggestions", List.of("Relax filter criteria", "Create a new donation"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        return ResponseEntity.ok(donations);
    }

    // ---------------------------------------------------
    // 8. Update donation
    // ---------------------------------------------------
    @PutMapping("/{donationId}")
    @Operation(
        summary = "Update donation",
        description = "Updates an existing donation (e.g., quantity, status, food items). Access by [DONOR, ADMIN]"
    )
    public ResponseEntity<?> updateDonation(
        @PathVariable String donationId,
        @RequestBody Donation updated
    ) {
        return donationService.updateDonation(donationId, updated)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                        "code", 404,
                        "message", "Cannot update. Donation not found for ID: " + donationId,
                        "suggestions", List.of("Verify donation ID", "Create a new donation"),
                        "timeStamp", LocalDateTime.now().toString()
                    )
                )
            );
    }

    // ---------------------------------------------------
    // 9. Delete / Cancel Donation
    // ---------------------------------------------------
    @DeleteMapping("/{donationId}")
    @Operation(
        summary = "Delete Donation",
        description = "Deletes a donation by its ID. Access by [ADMIN, DONOR]"
    )
    public ResponseEntity<?> deleteDonation(@PathVariable String donationId) {
        boolean deleted = donationService.deleteDonation(donationId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                    "code", 404,
                    "message", "Donation not found for ID: " + donationId,
                    "suggestions", List.of("Verify donation ID", "Create a new donation"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        return ResponseEntity.ok(
            Map.of(
                "code", 200,
                "message", "Donation ID: " + donationId + " successfully deleted",
                "timeStamp", LocalDateTime.now().toString()
            )
        );
    }
}
