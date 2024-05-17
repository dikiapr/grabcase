package com.example.demo.controller;

import com.example.demo.dto.request.AddCartDTO;
import com.example.demo.dto.request.FoodFilterRequestDTO;
import com.example.demo.service.FoodListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class FoodController {

    @Autowired
    private FoodListService foodListService;

    @GetMapping("/foods")
    public ResponseEntity<Object> getAllFoods(
            @PageableDefault(page = 0, size = 8, sort = "foodName", direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute FoodFilterRequestDTO foodFiltersDTO){
                return foodListService.getAllFoods(page, foodFiltersDTO);
    }

    @GetMapping("/foods/cart/data")
    public ResponseEntity<Object> getDataCarts(){
        return foodListService.getDataCart();
    }

    @PostMapping("/foods/cart/")
    public ResponseEntity<Object> addToCart(@RequestBody AddCartDTO foodId){
        return foodListService.addToCart(foodId);
    }

    @DeleteMapping("/foods/cart/{foodId}")
    public ResponseEntity<Object> deleteCart(@PathVariable(name = "foodId") Integer foodId){
        return foodListService.deleteCart(foodId);
    }
}









































