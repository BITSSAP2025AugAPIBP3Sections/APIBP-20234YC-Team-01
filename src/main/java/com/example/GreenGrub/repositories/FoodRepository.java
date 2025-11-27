package com.example.GreenGrub.repositories;

import com.example.GreenGrub.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, String>
{
}
