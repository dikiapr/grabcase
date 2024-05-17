package com.example.demo.repository;

import com.example.demo.model.Foods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Foods, Integer>, JpaSpecificationExecutor<Foods> {
//    @Query("SELECT r FROM Foods r WHERE r.foodId = :foodId ")
//    Optional<Foods> findByMyFoods(@Param("foodId") int foodId);
}
