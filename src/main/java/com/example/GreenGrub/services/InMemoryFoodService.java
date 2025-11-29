package com.example.GreenGrub.services;

import com.example.GreenGrub.entity.Food;
import com.example.GreenGrub.entity.FoodRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Profile("no-db")
public class InMemoryFoodService {

    private final Map<String, Food> foodStore = new ConcurrentHashMap<>();
    private final Map<String, FoodRequest> foodRequestStore = new ConcurrentHashMap<>();

    public Food postExcessFood(Food food) {
        // Generate ID if not provided
        if (food.getId() == null || food.getId().isBlank()) {
            food.setId("FOOD-" + UUID.randomUUID());
        }
        if (food.getCreatedAt() == null) {
            food.setCreatedAt(LocalDateTime.now());
        }
        food.setAvailable(true);
        foodStore.put(food.getId(), food);
        return food;
    }

    public List<Food> getAvailableFood() {
        LocalDateTime now = LocalDateTime.now();
        return foodStore.values().stream()
            .filter(Food::isAvailable)
            .collect(Collectors.toList());
    }

    public Optional<Food> getById(String id) {
        return Optional.ofNullable(foodStore.get(id));
    }

    public void markUnavailable(String id) {
        Food f = foodStore.get(id);
        if (f != null) {
            f.setAvailable(false);
        }
    }

    public FoodRequest requestFood(FoodRequest request) {
        if (request.getFoodRequestId() == null || request.getFoodRequestId().isBlank()) {
            request.setFoodRequestId("REQ-" + UUID.randomUUID());
        }
        if (request.getRequireOn() == null) {
            request.setRequireOn(LocalDateTime.now().plusHours(2));
        }

        foodRequestStore.put(request.getFoodRequestId(), request);
        return request;
    }

    public List<FoodRequest> getFoodRequests() {
        return new ArrayList<>(foodRequestStore.values());
    }

    public List<Food> filterFood(String category,
                                 LocalDateTime expiryAfter,
                                 String location) {
        return foodStore.values().stream()
            .filter(Food::isAvailable)
            .filter(f -> {
                if (category == null) return true;
                // case-insensitive compare – enum name or string field
                return f.getFoodType() != null
                    && f.getFoodType().name().equalsIgnoreCase(category);
            })
            .filter(f -> {
                if (expiryAfter == null || f.getExpiresAt() == null) return true;
                return f.getExpiresAt().isAfter(expiryAfter);
            })
            .toList();
    }

}
