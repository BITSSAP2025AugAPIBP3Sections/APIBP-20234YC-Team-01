package com.example.GreenGrub.controllers;

import com.example.GreenGrub.entity.Food;
import com.example.GreenGrub.repositories.TransactionRepository;
import com.example.GreenGrub.services.InMemoryFoodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // Post Excess Food
    @PostMapping("/postExcessFood")
    public ResponseEntity<Food> postExcessFood(@RequestBody Food food) {

        if (food == null) {
            return ResponseEntity.badRequest().build();
        }

        Food saved = foodService.postExcessFood(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Browse Available Food
    @GetMapping("/browseAvailableFood")
    public ResponseEntity<List<Food>> browseAvailableFood() {
        List<Food> availableFoodList = foodService.getAvailableFood();

        if (availableFoodList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableFoodList);
    }

    // ...keep your other methods as is for now
}
