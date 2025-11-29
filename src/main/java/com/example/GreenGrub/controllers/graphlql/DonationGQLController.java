package com.example.GreenGrub.controllers.graphlql;

import com.example.GreenGrub.dto.DonationReportDTO;
import com.example.GreenGrub.dto.MessageDTO;
import com.example.GreenGrub.entity.Donation;
import com.example.GreenGrub.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DonationGQLController {

    @Autowired
    private DonationService donationService;


    @QueryMapping
    public DonationReportDTO generateDonationReport(@Argument String userID) {
        return donationService.generateReport(userID);
    }

    @QueryMapping
    public List<Donation> donationHistory(@Argument String userId) {
        return donationService.getDonationHistory(userId);
    }

    @MutationMapping
    public Map<String, Object> shareDonationContacts(@Argument MessageDTO message) {
        String result = donationService.shareContacts(message);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        return response;
    }
}
