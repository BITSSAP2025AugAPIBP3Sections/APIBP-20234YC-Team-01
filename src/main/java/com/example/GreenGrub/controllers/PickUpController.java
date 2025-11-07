package com.example.GreenGrub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@Tag(name = "Delivery Management",description = "Endpoints for handle Delivery services like pick up, delivery and update donor/recipiant with estimated time of delivery")
public class PickUpController {

    @PutMapping("/{deliveryId}/changeDeliveryStatus")
    @Operation(summary = "Update delivery status", description = "This is to change the status of delivery")
    public ResponseEntity<String> updateTrackerStatus(@PathVariable String deliveryId,@RequestBody String tracker) {
        return ResponseEntity.ok().body(tracker.toString());
    }

    @PostMapping("/createDelivery")
    @Operation(summary = "Create Delivery", description = "Create a Delivery for a Donation")
    public ResponseEntity<String> createDelivery(@RequestBody String tracker) {
        return ResponseEntity.ok().body("Successfully created");
    }

    @GetMapping("/{userId}/getAllDeliveries")
    @Operation(summary = "Fetch all deliveries", description = "Get the history of all deliveries done by User")
    private ResponseEntity<String> getAllUserDeliveries(@PathVariable String userId) {
        return ResponseEntity.ok().body("Successfully retrieved deliveries for : "+userId);
    }

    @GetMapping("/{userId}/delivery/{deliveryId}")
    @Operation(summary = "Fetch details of a delivery", description = "Get the detail of a particular delivery")
    private ResponseEntity<String> getUserDelivery(@PathVariable String userId, @PathVariable String deliveryId){
        return ResponseEntity.ok().body("Successfully retrieved deliveries for : "+userId);
    }
}
