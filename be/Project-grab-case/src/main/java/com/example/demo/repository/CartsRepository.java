package com.example.demo.repository;

import com.example.demo.model.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartsRepository extends JpaRepository<Carts, Integer> {

    Carts findByFoodsFoodIdAndIsDeletedFalse(Integer foodId);
    List<Carts> findByIsDeletedFalse();
    @Query("SELECT COUNT(c) FROM Carts c WHERE c.isDeleted = false")
    Long countByIsDeletedFalse();
    @Query("SELECT SUM(c.foods.price * c.qty) FROM Carts c WHERE c.isDeleted = false")
    Integer totalPriceByFoodIdAndNotDeleted();
}
