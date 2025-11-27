package com.example.GreenGrub.repositories;

import com.example.GreenGrub.entity.FoodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, String> {
}

