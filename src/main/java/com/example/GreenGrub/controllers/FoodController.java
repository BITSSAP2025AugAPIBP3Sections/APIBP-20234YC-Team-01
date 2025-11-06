package com.example.GreenGrub.Controllers;

import com.example.GreenGrub.dtos.TransactionReviewDTO;
import com.example.GreenGrub.entity.Issue;
import com.example.GreenGrub.repositories.TransactionRepository;
import com.example.GreenGrub.entity.Food;
import com.example.GreenGrub.entity.FoodRequest;
import com.example.GreenGrub.entity.Transaction;
import com.example.GreenGrub.enumeration.FoodType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Profile("no-db")
@RequestMapping("/api/v1/food")
public class FoodController {

    @Autowired(required = false)
    TransactionRepository transactionRepository;

    // Post Excess Food
    @PostMapping("/postExcessFood")
    @Operation(summary = "Post Excess Food", description = "Allows users to post excess food they want to provide.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Food posted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> postExcessFood(@RequestBody Food food) {

        if (food == null) {
            return ResponseEntity.badRequest().body("Invalid request: food data is missing.");
        }

        // Business logic goes here

        return ResponseEntity.ok("Food posted successfully.");
    }

    // Browse Available Food
    @GetMapping("/browseAvailableFood")
    @Operation(summary = "Browse Available Food", description = "Allows users to browse available food listings.")
    public ResponseEntity<List<Food>> browseAvailableFood() {
        List<Food> availableFoodList = new ArrayList<>();

        // Business logic goes here

        if (availableFoodList.isEmpty()) {
            // Return 204 No Content when no food listings are available
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableFoodList);
    }

    // Request Food
    @PostMapping("/requestFood")
    @Operation(summary = "Request Food", description = "Allows users to request food from available listings.")
    public ResponseEntity<FoodRequest> requestFood(@RequestBody FoodRequest foodRequest) {

        if (foodRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic goes here

        return ResponseEntity.status(HttpStatus.CREATED).body(foodRequest);
    }

    // Browse Available Food
    @GetMapping("/browseFoodRequests")
    @Operation(summary = "Browse Food Requests", description = "Allows users to browse available food requests.")
    public ResponseEntity<List<Food>> getFoodRequests() {
        List<Food> availableFoodList = new ArrayList<>();

        // Business logic goes here

        if (availableFoodList.isEmpty()) {
            // Return 204 No Content when no food listings are available
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableFoodList);
    }

    // Rate / Review Transaction for food request
    @PostMapping("/{foodRequestId}/rateReviewTransaction")
    @Operation(summary = "Rate and Review Transaction for Food Request", description = "Allows users to rate and review transactions.")
    public ResponseEntity<String> rateReviewTransactionForFoodRequest(@Valid @RequestBody TransactionReviewDTO reviewDTO, @PathVariable String foodRequestId) {

        Optional<Transaction> optionalTransaction = transactionRepository.findById(reviewDTO.getTransactionId());

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.badRequest().body("Transaction not found with ID: " + reviewDTO.getTransactionId());
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setRating(reviewDTO.getRating());
        transaction.setReview(reviewDTO.getReview());

        transactionRepository.save(transaction);

        return ResponseEntity.ok("Transaction rated and reviewed successfully.");
    }

    // Rate / Review Transaction for excess food post
    @PostMapping("/{foodId}/rateReviewTransaction")
    @Operation(summary = "Rate and Review Transaction for excess Food Post", description = "Allows users to rate and review transactions.")
    public ResponseEntity<String> rateReviewTransactionForExcessFoodPost(@Valid @RequestBody TransactionReviewDTO reviewDTO, @PathVariable String foodId) {

        Optional<Transaction> optionalTransaction = transactionRepository.findById(reviewDTO.getTransactionId());

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.badRequest().body("Transaction not found with ID: " + reviewDTO.getTransactionId());
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setRating(reviewDTO.getRating());
        transaction.setReview(reviewDTO.getReview());

        transactionRepository.save(transaction);

        return ResponseEntity.ok("Transaction rated and reviewed successfully.");
    }

    // Report Food Condition / Issue for Excess Food Post
    @PostMapping("/{foodId}/reportFoodIssue")
    @Operation(summary = "Report Food Issue", description = "Allows users to report issues with food items.")
    public ResponseEntity<String> reportFoodIssueForExcessFoodPost(@RequestBody Issue issue, @PathVariable String foodId) {

        // Business logic will go here

        return ResponseEntity.ok("Food issue reported successfully.");
    }

    // Report Food Condition / Issue for Food Request
    @PostMapping("/{foodRequest}/reportFoodIssue")
    @Operation(summary = "Report Food Issue", description = "Allows users to report issues with food items.")
    public ResponseEntity<String> reportFoodIssueForExcessFoodPost(@RequestBody Issue issue, @PathVariable FoodRequest foodRequest) {

        // Business logic will go here

        return ResponseEntity.ok("Food issue reported successfully.");
    }


    // Filter by Category / Expiry / Location
    @GetMapping("/filterFood")
    @Operation(summary = "Filter Food", description = "Allows users to filter food listings by category, expiry date, and location.")
    public List<Food> filterFood(@RequestParam(required = false) FoodType category,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryAfter,
                                 @RequestParam(required = false) String location) {

        // Business logic goes here

        return new ArrayList<>();
    }
}
