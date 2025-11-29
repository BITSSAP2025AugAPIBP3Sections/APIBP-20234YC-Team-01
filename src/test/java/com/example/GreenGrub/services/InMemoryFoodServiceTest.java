package com.example.GreenGrub.services;

import com.example.GreenGrub.entity.Food;
import com.example.GreenGrub.enumeration.FoodType;
import com.example.GreenGrub.enumeration.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFoodServiceTest {
    
    private InMemoryFoodService foodService;

    @BeforeEach
    void setUp() {
        foodService = new InMemoryFoodService();
    }

    @Test
    void postExcessFood_shouldGenerateIdIfNotProvided() {
        Food food = Food.builder()
                .name("Pizza")
                .description("Delicious pizza")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .build();
        
        Food savedFood = foodService.postExcessFood(food);
        
        assertNotNull(savedFood.getId());
        assertFalse(savedFood.getId().isBlank());
        assertEquals("Pizza", savedFood.getName());
        assertTrue(savedFood.isAvailable());
        assertNotNull(savedFood.getCreatedAt());
    }

    @Test
    void postExcessFood_shouldUseProvidedId() {
        Food food = Food.builder()
                .id("custom-id-123")
                .name("Bread")
                .description("Fresh bread")
                .quantityAvailable(10)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .build();
        
        Food savedFood = foodService.postExcessFood(food);
        
        assertEquals("custom-id-123", savedFood.getId());
        assertEquals("Bread", savedFood.getName());
    }

    @Test
    void getAvailableFood_shouldReturnOnlyAvailableFood() {
        Food food1 = Food.builder()
                .name("Pizza")
                .description("Delicious pizza")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .expiresAt(LocalDateTime.now().plusDays(2))
                .build();
        
        Food food2 = Food.builder()
                .name("Bread")
                .description("Fresh bread")
                .quantityAvailable(3)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        
        Food savedFood1 = foodService.postExcessFood(food1);
        Food savedFood2 = foodService.postExcessFood(food2);
        
        // Mark one as unavailable
        foodService.markUnavailable(savedFood2.getId());
        
        List<Food> availableFood = foodService.getAvailableFood();
        
        assertEquals(1, availableFood.size());
        assertEquals("Pizza", availableFood.get(0).getName());
    }

    @Test
    void getAvailableFood_shouldFilterExpiredFood() {
        Food expiredFood = Food.builder()
                .name("Expired Pizza")
                .description("Old pizza")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();
        
        Food freshFood = Food.builder()
                .name("Fresh Bread")
                .description("Fresh bread")
                .quantityAvailable(3)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        
        foodService.postExcessFood(expiredFood);
        foodService.postExcessFood(freshFood);
        
        List<Food> availableFood = foodService.getAvailableFood();
        
        assertEquals(1, availableFood.size());
        assertEquals("Fresh Bread", availableFood.get(0).getName());
    }

    @Test
    void getAvailableFood_shouldIncludeFoodWithNullExpiration() {
        Food foodWithoutExpiry = Food.builder()
                .name("Canned Beans")
                .description("Canned food")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .build();
        
        foodService.postExcessFood(foodWithoutExpiry);
        
        List<Food> availableFood = foodService.getAvailableFood();
        
        assertEquals(1, availableFood.size());
        assertEquals("Canned Beans", availableFood.get(0).getName());
    }

    @Test
    void getById_shouldReturnFoodIfExists() {
        Food food = Food.builder()
                .name("Pizza")
                .description("Delicious pizza")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .build();
        
        Food savedFood = foodService.postExcessFood(food);
        
        Optional<Food> retrieved = foodService.getById(savedFood.getId());
        
        assertTrue(retrieved.isPresent());
        assertEquals("Pizza", retrieved.get().getName());
    }

    @Test
    void getById_shouldReturnEmptyIfNotExists() {
        Optional<Food> retrieved = foodService.getById("non-existent-id");
        
        assertFalse(retrieved.isPresent());
    }

    @Test
    void markUnavailable_shouldSetAvailableToFalse() {
        Food food = Food.builder()
                .name("Pizza")
                .description("Delicious pizza")
                .quantityAvailable(5)
                .unitType(UnitType.KiloGram)
                .foodType(FoodType.VEG)
                .build();
        
        Food savedFood = foodService.postExcessFood(food);
        assertTrue(savedFood.isAvailable());
        
        foodService.markUnavailable(savedFood.getId());
        
        Optional<Food> retrieved = foodService.getById(savedFood.getId());
        assertTrue(retrieved.isPresent());
        assertFalse(retrieved.get().isAvailable());
    }

    @Test
    void markUnavailable_shouldHandleNonExistentId() {
        assertDoesNotThrow(() -> foodService.markUnavailable("non-existent-id"));
    }

    @Test
    void getAvailableFood_shouldReturnEmptyListWhenNoFoodAvailable() {
        List<Food> availableFood = foodService.getAvailableFood();
        
        assertTrue(availableFood.isEmpty());
    }
}
