package com.example.GreenGrub.services;

import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.enumeration.DonationStatus;
import com.example.GreenGrub.repositories.DonationRepository;
import com.example.GreenGrub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DonationServiceTest {
    
    @Mock
    private DonationRepository donationRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private DonationService donationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateReport_shouldReturnReportForAllDonationsWhenUserIdIsNull() {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(10).build(),
                Donation.builder().donorId("donor2").quantity(20).build()
        );
        when(donationRepository.findAll()).thenReturn(donations);
        
        DonationReportDTO report = donationService.generateReport(null);
        
        assertEquals("ALL", report.getOrganizationId());
        assertEquals(2, report.getTotalDonations());
        assertEquals(30.0, report.getTotalFoodDonated());
    }

    @Test
    void generateReport_shouldReturnReportForAllDonationsWhenUserIdIsEmpty() {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(15).build()
        );
        when(donationRepository.findAll()).thenReturn(donations);
        
        DonationReportDTO report = donationService.generateReport("");
        
        // When userId is empty string (not null), the organizationId is set to the empty string value
        assertEquals("", report.getOrganizationId());
        assertEquals(1, report.getTotalDonations());
        assertEquals(15.0, report.getTotalFoodDonated());
    }

    @Test
    void generateReport_shouldReturnReportForSpecificDonor() {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(25).build(),
                Donation.builder().donorId("donor1").quantity(15).build()
        );
        when(donationRepository.findByDonorId("donor1")).thenReturn(donations);
        
        DonationReportDTO report = donationService.generateReport("donor1");
        
        assertEquals("donor1", report.getOrganizationId());
        assertEquals(2, report.getTotalDonations());
        assertEquals(40.0, report.getTotalFoodDonated());
    }

    @Test
    void generateReport_shouldHandleNullQuantities() {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(null).build(),
                Donation.builder().donorId("donor1").quantity(10).build()
        );
        when(donationRepository.findByDonorId("donor1")).thenReturn(donations);
        
        DonationReportDTO report = donationService.generateReport("donor1");
        
        assertEquals(2, report.getTotalDonations());
        assertEquals(10.0, report.getTotalFoodDonated());
    }

    @Test
    void shareContacts_shouldReturnNotFoundForInvalidDonationId() {
        MessageDTO message = MessageDTO.builder()
                .donationId("invalid-id")
                .senderId("sender1")
                .build();
        when(donationRepository.findById("invalid-id")).thenReturn(Optional.empty());
        
        String result = donationService.shareContacts(message);
        
        assertTrue(result.contains("Donation not found"));
    }

    @Test
    void shareContacts_shouldApproveDonationAndShareContacts() {
        Donation donation = Donation.builder()
                .donationId("donation1")
                .donorId("donor1")
                .recipientId("recipient1")
                .status(DonationStatus.PENDING)
                .quantity(10)
                .build();
        
        User donor = User.builder().id("donor1").phoneNumber("1111111111").build();
        User recipient = User.builder().id("recipient1").phoneNumber("2222222222").build();
        
        MessageDTO message = MessageDTO.builder()
                .donationId("donation1")
                .senderId("donor1")
                .message("APPROVE")
                .build();
        
        when(donationRepository.findById("donation1")).thenReturn(Optional.of(donation));
        when(userRepository.findById("donor1")).thenReturn(Optional.of(donor));
        when(userRepository.findById("recipient1")).thenReturn(Optional.of(recipient));
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);
        
        String result = donationService.shareContacts(message);
        
        assertTrue(result.contains("Donation approved"));
        assertTrue(result.contains("1111111111"));
        assertTrue(result.contains("2222222222"));
        verify(donationRepository).save(donation);
        assertEquals(DonationStatus.ACCEPTED, donation.getStatus());
    }

    @Test
    void shareContacts_shouldShareContactsForAcceptedDonation() {
        Donation donation = Donation.builder()
                .donationId("donation1")
                .donorId("donor1")
                .recipientId("recipient1")
                .status(DonationStatus.ACCEPTED)
                .quantity(10)
                .build();
        
        User recipient = User.builder().id("recipient1").phoneNumber("2222222222").build();
        
        MessageDTO message = MessageDTO.builder()
                .donationId("donation1")
                .senderId("donor1")
                .build();
        
        when(donationRepository.findById("donation1")).thenReturn(Optional.of(donation));
        when(userRepository.findById("recipient1")).thenReturn(Optional.of(recipient));
        
        String result = donationService.shareContacts(message);
        
        assertTrue(result.contains("Recipient contact shared") || result.contains("2222222222"));
    }

    @Test
    void shareContacts_shouldReturnUnavailableForCancelledDonation() {
        Donation donation = Donation.builder()
                .donationId("donation1")
                .donorId("donor1")
                .status(DonationStatus.CANCELLED)
                .quantity(0)
                .build();
        
        MessageDTO message = MessageDTO.builder()
                .donationId("donation1")
                .senderId("recipient1")
                .build();
        
        when(donationRepository.findById("donation1")).thenReturn(Optional.of(donation));
        
        String result = donationService.shareContacts(message);
        
        assertTrue(result.contains("not available"));
    }

    @Test
    void shareContacts_shouldMaskContactForPendingRequest() {
        Donation donation = Donation.builder()
                .donationId("donation1")
                .donorId("donor1")
                .recipientId("recipient1")
                .status(DonationStatus.PENDING)
                .quantity(10)
                .build();
        
        User donor = User.builder().id("donor1").phoneNumber("1234567890").build();
        
        MessageDTO message = MessageDTO.builder()
                .donationId("donation1")
                .senderId("recipient1")
                .build();
        
        when(donationRepository.findById("donation1")).thenReturn(Optional.of(donation));
        when(userRepository.findById("donor1")).thenReturn(Optional.of(donor));
        
        String result = donationService.shareContacts(message);
        
        assertTrue(result.contains("not approved"));
        assertTrue(result.contains("****7890"));
    }

    @Test
    void shareContacts_shouldSetTimestampIfNull() {
        Donation donation = Donation.builder()
                .donationId("donation1")
                .donorId("donor1")
                .status(DonationStatus.CANCELLED)
                .build();
        
        MessageDTO message = MessageDTO.builder()
                .donationId("donation1")
                .senderId("sender1")
                .timestamp(null)
                .build();
        
        when(donationRepository.findById("donation1")).thenReturn(Optional.of(donation));
        
        donationService.shareContacts(message);
        
        assertNotNull(message.getTimestamp());
        assertTrue(message.getTimestamp() > 0);
    }

    @Test
    void generateCsvReportBytes_shouldGenerateValidCsv() throws Exception {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(10).build(),
                Donation.builder().donorId("donor1").quantity(15).build()
        );
        when(donationRepository.findByDonorId("donor1")).thenReturn(donations);
        
        byte[] csvBytes = donationService.generateCsvReportBytes("donor1");
        String csv = new String(csvBytes, StandardCharsets.UTF_8);
        
        assertTrue(csv.contains("organizationId,totalDonations,totalFoodDonated"));
        assertTrue(csv.contains("donor1"));
        assertTrue(csv.contains("2"));
        assertTrue(csv.contains("25.00"));
    }

    @Test
    void generateCsvReportBytes_shouldHandleNullUserId() throws Exception {
        List<Donation> donations = Collections.emptyList();
        when(donationRepository.findAll()).thenReturn(donations);
        
        byte[] csvBytes = donationService.generateCsvReportBytes(null);
        String csv = new String(csvBytes, StandardCharsets.UTF_8);
        
        assertTrue(csv.contains("ALL"));
        assertTrue(csv.contains("0"));
    }

    @Test
    void getDonationHistory_shouldReturnDonationsForUser() {
        List<Donation> donations = Arrays.asList(
                Donation.builder().donorId("donor1").quantity(10).build(),
                Donation.builder().donorId("donor1").quantity(20).build()
        );
        when(donationRepository.findByDonorId("donor1")).thenReturn(donations);
        
        List<Donation> history = donationService.getDonationHistory("donor1");
        
        assertEquals(2, history.size());
        verify(donationRepository).findByDonorId("donor1");
    }

    @Test
    void getDonationHistory_shouldReturnEmptyListForNullUserId() {
        List<Donation> history = donationService.getDonationHistory(null);
        
        assertTrue(history.isEmpty());
        verify(donationRepository, never()).findByDonorId(anyString());
    }
}
