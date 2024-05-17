package com.example.demo.service;

import com.example.demo.dto.request.AddCartDTO;
import com.example.demo.dto.request.FoodFilterRequestDTO;
import com.example.demo.dto.response.FoodResponseDTO;
import com.example.demo.dto.response.ResponseBodyDTO;
import com.example.demo.exception.classes.DataNotFoundException;
import com.example.demo.model.Carts;
import com.example.demo.model.Foods;
import com.example.demo.repository.CartsRepository;
import com.example.demo.repository.FoodRepository;
import com.example.demo.utils.ValidationMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodListService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private ValidationMessageUtils messageUtils;

    public ResponseEntity<Object> getAllFoods(Pageable page, FoodFilterRequestDTO foodFiltersDTO){

        HttpStatus status = HttpStatus.CREATED;
        String message = messageUtils.generateLoadData();
        Page<Foods> foodsPage = foodRepository.findAll(page);

        try{
            List<FoodResponseDTO> foodResponseList = foodsPage.getContent().stream()
                    .map(food -> new FoodResponseDTO(
                            food.getFoodId(),
                            food.getPrice(),
                            food.getFoodName(),
                            food.getLocation(),
                            food.getImageFilename()
                    ))
                    .collect(Collectors.toList());

            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total(foodsPage.getTotalElements())
                    .data(foodResponseList)
                    .message(message)
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK.name())
                    .build();

            return ResponseEntity.ok(result);
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateServerErrorMessage();
            throw new DataNotFoundException(message);
        }
    }

    public ResponseEntity<Object> addToCart(AddCartDTO addCartDTO){

        HttpStatus status = HttpStatus.CREATED;
        String message = messageUtils.generateSuccessAddToCartMessage();

        try{
            Integer foodId = addCartDTO.getFoodId();

            Carts existingCart = cartsRepository.findByFoodsFoodIdAndIsDeletedFalse(foodId);
            if(existingCart != null) {

                status = HttpStatus.BAD_REQUEST;
                message = messageUtils.generateFailedDataExistCartErrorMessage();
                ResponseBodyDTO errorResult = ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build();
                return ResponseEntity.status(status).body(errorResult);
            }

            Carts cart = Carts.builder()
                    .foods(Foods.builder().foodId(foodId).build())
                    .qty(1)
                    .isDeleted(false)
                    .createdBy("user")
                    .build();

            cartsRepository.save(cart);

            Map<String, Object> responseData = buildResponseData(foodId);

            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total(1)
                    .data(responseData)
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(result);
        } catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateFailedCartErrorMessage();
            ResponseBodyDTO errorResult = ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(errorResult);
        }
    }

    public Map<String, Object> buildResponseData(Integer foodId) {
        HttpStatus status = HttpStatus.CREATED;
        String message = messageUtils.generateServerErrorMessage();

        Map<String, Object> responseData = new HashMap<>();
        Optional<Foods> foodsDataOptional = foodRepository.findById(foodId);
        if(foodsDataOptional.isPresent()){
            Foods foodsData = foodsDataOptional.get();

            responseData.put("foodId", foodsData.getFoodId());
            responseData.put("nama_makanan", foodsData.getFoodName());
            responseData.put("harga", foodsData.getPrice());
            responseData.put("location", foodsData.getLocation());
            responseData.put("is_cart", true);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateServerErrorMessage();
            throw new DataNotFoundException(message);
        }
        return responseData;
    }

    public ResponseEntity<Object> deleteCart(Integer foodId){
        HttpStatus status = HttpStatus.CREATED;
        String message = messageUtils.generateSuccessDeleteCartMessage();

        Carts cartData = cartsRepository.findByFoodsFoodIdAndIsDeletedFalse(foodId);

        if(cartData != null){
            cartData.setIsDeleted(true);
            cartsRepository.save(cartData);
            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total(1)
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(result);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateFailedDeleteCartErrorMessage();
            throw new DataNotFoundException(message);
        }
    }

    public ResponseEntity<Object> getDataCart(){
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessGetDataCartMessage();

        try{
            List<Carts> cartsList = cartsRepository.findByIsDeletedFalse();
            List<Map<String, Object>> cartsDataList = new ArrayList<>();
            for(Carts cart : cartsList){
                Map<String, Object> cartData = new HashMap<>();
                cartData.put("foodId", cart.getFoods().getFoodId());
                cartData.put("foodName", cart.getFoods().getFoodName());
                cartData.put("location", cart.getFoods().getLocation());
                cartData.put("price", cart.getFoods().getPrice());
                cartsDataList.add(cartData);
            }

            Long total = cartsRepository.countByIsDeletedFalse();

            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total(total)
                    .data(cartsDataList)
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(result);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateFailedDataCartErrorMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }
}





















