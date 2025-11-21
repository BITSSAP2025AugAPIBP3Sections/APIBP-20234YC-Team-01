package com.example.GreenGrub.services;

import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.dto.UserProfile;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.enumeration.DonationStatus;
import com.example.GreenGrub.repositories.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    /**
     * Generate a simple report DTO for a donor (if userId provided) or for all donations (if userId is null/empty).
     * This method is compatible with your DonationReportDTO which has organizationId, totalDonations and totalFoodDonated.
     */
    public DonationReportDTO generateReport(String userId) {

        List<Donation> donations;

        if (userId == null || userId.trim().isEmpty()) {
            donations = donationRepository.findAll();
        } else {
            donations = donationRepository.findByDonorId(userId);
        }

        DonationReportDTO report = new DonationReportDTO();
        report.setOrganizationId(userId == null ? "ALL" : userId);
        report.setTotalDonations(donations.size());

        double totalFoodDonated = donations.stream()
            .mapToDouble(d -> d.getQuantity() == null ? 0 : d.getQuantity())
            .sum();
        report.setTotalFoodDonated(totalFoodDonated);

        return report;
    }

    /**
     * Share contact info depending on donation state and who requested.
     * Uses MessageDTO fields: senderId, receiverId, donationId.
     */
    public String shareContacts(MessageDTO message) {

        if (message.getTimestamp() == null || message.getTimestamp() == 0L) {
            message.setTimestamp(System.currentTimeMillis());
        }

        Optional<Donation> opt = donationRepository.findById(message.getDonationId());
        if (opt.isEmpty()) {
            return "Donation not found for ID: " + message.getDonationId();
        }

        Donation donation = opt.get();

        String donorId = donation.getDonorId();
        String recipientId = donation.getRecipientId();
        String senderId = message.getSenderId();

        // Safe contact lookup
        java.util.function.Function<String, String> safeContact = (idStr) -> {
            if (idStr == null || idStr.trim().isEmpty()) return "Unavailable";
            try {
                long id = Long.parseLong(idStr);
                String contact = UserProfile.getContact(id);
                return (contact == null || contact.trim().isEmpty()) ? "Unavailable" : contact;
            } catch (Exception ex) {
                return "Unavailable";
            }
        };

        boolean acceptedAndAssigned =
            donation.getStatus() == DonationStatus.ACCEPTED &&
                recipientId != null && !recipientId.trim().isEmpty();

        // CASE 1: Donation is accepted and contact should be exchanged
        if (acceptedAndAssigned) {

            String donorContact = safeContact.apply(donorId);
            String recipientContact = safeContact.apply(recipientId);

            if (senderId != null && senderId.equals(donorId)) {
                return "Recipient contact shared.\nRecipient Phone: " + recipientContact;
            }

            if (senderId != null && senderId.equals(recipientId)) {
                return "Donor contact shared.\nDonor Phone: " + donorContact;
            }

            return "Contact details have been exchanged between the donor and the recipient.";
        }

        // CASE 2: Donation not available (cancelled or zero quantity)
        boolean available = donation.getQuantity() != null
            && donation.getQuantity() > 0
            && donation.getStatus() != DonationStatus.CANCELLED;

        if (!available) {
            return "This donation is not available. It may be cancelled or no food may be remaining.";
        }

        // CASE 3: Sender is donor checking before approval
        if (senderId != null && senderId.equals(donorId)) {
            return "You are the donor. The recipient's contact will be shared once you approve their request.";
        }

        // CASE 4: Sender is recipient, but donation not yet approved
        String donorContact = safeContact.apply(donorId);
        String masked = maskContact(donorContact);

        return "The donor has not approved your request yet.\n"
            + "Partial Donor Contact: " + masked + "\n"
            + "Full contact details will be shared after donor approval.";
    }

    private String maskContact(String contact) {
        if (contact == null || contact.equals("Unavailable")) return "Unavailable";
        if (contact.length() <= 4) return "****";
        String lastFour = contact.substring(contact.length() - 4);
        return "****" + lastFour;
    }

    /**
     * Create a minimal CSV bytes for the summary report (organizationId,totalDonations,totalFoodDonated).
     * This uses generateReport(...) to ensure the CSV matches DonationReportDTO exactly.
     */
    public byte[] generateCsvReportBytes(String userId) throws Exception {
        DonationReportDTO report = generateReport(userId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            // header matching DonationReportDTO fields
            writer.write("organizationId,totalDonations,totalFoodDonated\n");

            String org = report.getOrganizationId() == null ? "" : report.getOrganizationId();
            String totalDon = String.valueOf(report.getTotalDonations());
            // ensure decimal uses dot and has no grouping
            String totalFood = String.format(Locale.ROOT, "%.2f", report.getTotalFoodDonated());

            // safe CSV values (no fancy escaping needed for these fields but keep helper for future-proof)
            writer.write(String.format("%s,%s,%s\n", safeCsv(org), safeCsv(totalDon), safeCsv(totalFood)));
            writer.flush();
        }
        return baos.toByteArray();
    }

    private String safeCsv(String s) {
        if (s == null) return "";
        String escaped = s.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        } else {
            return escaped;
        }
    }

    /**
     * Return donation history for donor (simple wrapper).
     */
    public List<Donation> getDonationHistory(String userId) {
        if (userId == null) return Collections.emptyList();
        return donationRepository.findByDonorId(userId);
    }
}