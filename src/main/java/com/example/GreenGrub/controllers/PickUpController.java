package com.example.GreenGrub.controllers;

import com.example.GreenGrub.dto.TrackStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
public class PickUpController {

    @PutMapping("/{deliveryId}/changeDeliveryStatus")
    @Operation(description = "This is to change the status of delivery")
    public ResponseEntity<String> updateTrackerStatus(@PathVariable String deliveryId,@RequestBody TrackStatusRequest tracker) {
        return ResponseEntity.ok().body(tracker.toString());
    }

    @PostMapping("/createDelivery")
    @Operation(description = "Create a Delivery for a Donation")
    public ResponseEntity<String> createDelivery(@RequestBody TrackStatusRequest tracker) {
        return ResponseEntity.ok().body("Successfully created");
    }

    @GetMapping("/{userId}/getAllDeliveries")
    private ResponseEntity<String> getAllUserDeliveries(@PathVariable String userId) {
        return ResponseEntity.ok().body("Successfully retrieved deliveries for : "+userId);
    }

    @GetMapping("/{userId}/delivery/{deliveryId}")
    private ResponseEntity<String> getUserDelivery(@PathVariable String userId, @PathVariable String deliveryId){
        return ResponseEntity.ok().body("Successfully retrieved deliveries for : "+userId);
    }
}
