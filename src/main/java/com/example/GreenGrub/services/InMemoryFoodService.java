package com.example.GreenGrub.services;

import com.example.GreenGrub.entity.Food;
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

    public Food postExcessFood(Food food) {
        // Generate ID if not provided
        if (food.getId() == null || food.getId().isBlank()) {
            food.setId(UUID.randomUUID().toString());
        }
        // Set defaults
        food.setCreatedAt(LocalDateTime.now());
        food.setAvailable(true);  // uses Lombok @Data -> setter is setAvailable
        // Store
        foodStore.put(food.getId(), food);
        return food;
    }

    public List<Food> getAvailableFood() {
        LocalDateTime now = LocalDateTime.now();
        return foodStore.values().stream()
            .filter(Food::isAvailable)
            .filter(f -> f.getExpiresAt() == null || f.getExpiresAt().isAfter(now))
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
}
