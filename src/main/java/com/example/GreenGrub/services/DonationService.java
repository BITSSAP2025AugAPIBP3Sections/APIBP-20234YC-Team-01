package com.example.GreenGrub.services;

import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.dto.UserProfile;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.enumeration.DonationStatus;
import com.example.GreenGrub.repositories.DonationRepository;
import com.example.GreenGrub.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

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
        String msg = message.getMessage() == null ? "" : message.getMessage().trim().toUpperCase();

        // Safe contact lookup
        java.util.function.Function<String, String> safeContact = (idStr) -> {
            if (idStr == null || idStr.trim().isEmpty()) return "Unavailable";
            try {
                final var user= userRepository.findById(idStr).orElse(null);
                String contact = "";
                if(user!=null){
                    contact = user.getPhoneNumber();
                }
                return (contact == null || contact.trim().isEmpty()) ? "Unavailable" : contact;
            } catch (Exception ex) {
                return "Unavailable";
            }
        };

        boolean isDonor = senderId != null && senderId.equals(donorId);
        boolean hasRecipient = recipientId != null && !recipientId.trim().isEmpty();

        if (isDonor
            && "APPROVE".equals(msg)
            && donation.getStatus() == DonationStatus.PENDING
            && hasRecipient) {

            donation.setStatus(DonationStatus.ACCEPTED);
            donationRepository.save(donation);

            String donorContact = safeContact.apply(donorId);
            String recipientContact = safeContact.apply(recipientId);

            return "Donation approved.\n"
                + "Donor Phone: " + donorContact + "\n"
                + "Recipient Phone: " + recipientContact;
        }

        // CASE 1: Donation is accepted and contact should be exchanged
        boolean acceptedAndAssigned =
            donation.getStatus() == DonationStatus.ACCEPTED &&
                hasRecipient;

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

        // CASE 2: Donation not available
        boolean available = donation.getQuantity() != null
            && donation.getQuantity() > 0
            && donation.getStatus() != DonationStatus.CANCELLED;

        if (!available) {
            return "This donation is not available. It may be cancelled or no food may be remaining.";
        }

        // CASE 3: Sender is donor checking before approval (but didn't send APPROVE)
        if (isDonor) {
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

    public Donation createDonation(Donation donation) {
        // set timestamps or defaults if needed
        if (donation.getCreatedAt() == null) {
            donation.setCreatedAt(LocalDateTime.now());
        }
        donation.setUpdatedAt(LocalDateTime.now());
        return donationRepository.save(donation);
    }

    public Optional<Donation> getDonationById(String donationId) {
        return donationRepository.findById(donationId);
    }

    public List<Donation> listDonations(String donorId, String recipientId, String status) {
        List<Donation> all = donationRepository.findAll();

        return all.stream()
            .filter(d -> donorId == null || donorId.equals(d.getDonorId()))
            .filter(d -> recipientId == null || recipientId.equals(d.getRecipientId()))
            .filter(d -> status == null || status.equalsIgnoreCase(d.getStatus().name()))
            .toList();
    }

    public Optional<Donation> updateDonation(String donationId, Donation request) {
        return donationRepository.findById(donationId)
            .map(existing -> {

                // only update if not null
                if (request.getStatus() != null) {
                    existing.setStatus(request.getStatus());
                }
                if (request.getQuantity() != null) {
                    existing.setQuantity(request.getQuantity());
                }
                if (request.getRecipientId() != null) {
                    existing.setRecipientId(request.getRecipientId());
                }
                if (request.getWebsiteUrl() != null) {
                    existing.setWebsiteUrl(request.getWebsiteUrl());
                }
                if (request.getPickupAddress() != null) {
                    existing.setPickupAddress(request.getPickupAddress());
                }
                if (request.getDeliveryAddress() != null) {
                    existing.setDeliveryAddress(request.getDeliveryAddress());
                }

                existing.setUpdatedAt(LocalDateTime.now());

                return donationRepository.save(existing);
            });
    }

    public boolean deleteDonation(String donationId) {
        if (!donationRepository.existsById(donationId)) {
            return false;
        }
        donationRepository.deleteById(donationId);
        return true;
    }
}