package com.example.GreenGrub.controllers;

import com.example.GreenGrub.entity.Food;
import com.example.GreenGrub.entity.FoodRequest;
import com.example.GreenGrub.services.InMemoryFoodService;
import com.example.GreenGrub.repositories.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Profile("no-db")
@RequestMapping("/api/v1/food")
@Tag(name = "Food Management" , description = "Endpoints for creating and process food for donation, Access by [RECIPIENT,DONOR]")
public class FoodController {

    private final InMemoryFoodService foodService;

    @Autowired(required = false)
    TransactionRepository transactionRepository;

    public FoodController(InMemoryFoodService foodService) {
        this.foodService = foodService;
    }

    // ---------------------------
    // 1) Post Excess Food (DONOR)
    // ---------------------------
    @PostMapping("/postExcessFood")
    @Operation(summary = "Post Excess Food", description = "Allows donors to post excess food.")
    public ResponseEntity<Food> postExcessFood(@RequestBody Food food) {

        if (food == null) {
            return ResponseEntity.badRequest().build();
        }

        Food saved = foodService.postExcessFood(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------------------------------
    // 2) Browse Available Food (GENERIC)
    // ---------------------------------
    @GetMapping("/browseAvailableFood")
    @Operation(summary = "Browse Available Food", description = "Allows users to browse available food listings.")
    public ResponseEntity<List<Food>> browseAvailableFood() {
        List<Food> availableFoodList = foodService.getAvailableFood();

        if (availableFoodList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableFoodList);
    }

    @PostMapping("/requestFood")
    @Operation(summary = "Request Food", description = "Allows recipients to request food from available listings.")
    public ResponseEntity<FoodRequest> requestFood(@RequestBody FoodRequest foodRequest) {

        if (foodRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        if (foodRequest.getFoodRequestId() == null || foodRequest.getFoodRequestId().isBlank()) {
            foodRequest.setFoodRequestId("REQ-" + UUID.randomUUID());
        }
        if (foodRequest.getRequireOn() == null) {
            foodRequest.setRequireOn(LocalDateTime.now().plusHours(2));
        }

        FoodRequest saved = foodService.requestFood(foodRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/browseFoodRequests")
    @Operation(summary = "Browse Food Requests", description = "Allows donors to browse active food requests.")
    public ResponseEntity<List<FoodRequest>> browseFoodRequests() {
        List<FoodRequest> requests = foodService.getFoodRequests();
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/filterFood")
    @Operation(
        summary = "Filter Food (in-memory)",
        description = "Filter in-memory food listings by category, expiry date, and location."
    )
    public ResponseEntity<List<Food>> filterFood(
        @RequestParam(required = false) String category,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime expiryAfter,
        @RequestParam(required = false) String location
    ) {
        List<Food> filtered = foodService.filterFood(category, expiryAfter, location);

        if (filtered.isEmpty()) {
            return ResponseEntity.ok(
                (List<Food>) Map.of(
                    "code", 404,
                    "message", "No food items match the filter criteria",
                    "suggestions", List.of("Try different filter values", "Check back later"),
                    "timeStamp", LocalDateTime.now().toString()
                )
            );
        }

        return ResponseEntity.ok(filtered);
    }
}
