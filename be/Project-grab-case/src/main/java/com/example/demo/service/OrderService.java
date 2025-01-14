package com.example.demo.service;

import com.example.demo.dto.response.ResponseBodyDTO;
import com.example.demo.kafka.OrderKafkaProducer;
import com.example.demo.model.Carts;
import com.example.demo.model.Orders;
import com.example.demo.repository.CartsRepository;
import com.example.demo.repository.OrderMongoDBRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.ValidationMessageUtils;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private ValidationMessageUtils messageUtils;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    public ResponseEntity<Object> createOrder(){
        HttpStatus status = HttpStatus.CREATED;
        String message = messageUtils.generateSuccessCreateOrderMessage();

        try{
            Long totalItems = cartsRepository.countByIsDeletedFalse();
            Integer totalOrderPrice = cartsRepository.totalPriceByFoodIdAndNotDeleted();

            Date currentDate = new Date();
            currentDate = removeTime(currentDate);

            Orders order = Orders.builder()
                    .paxIdGsi(1)
                    .orderDate(currentDate)
                    .createdBy("John Doe")
                    .modifiedBy("John Doe")
                    .paxId("PAX001")
                    .state("ongoing")
                    .totalItem(totalItems.intValue())
                    .totalOrderPrice(totalOrderPrice)
                    .build();

            orderRepository.save(order);
            orderKafkaProducer.sendOrder(order);

            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        } catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateErrorCreateOrderMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    public ResponseEntity<Object> updateOrder(int orderId){
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessUpdateOrderMessage();

        try{
            Optional<Orders> orderOptional = orderRepository.findById(orderId);
            if(orderOptional.isPresent()) {
                Orders order = orderOptional.get();

                order.setState("complete");
                orderRepository.save(order);

                orderKafkaProducer.updateOrderStatus(order);

                return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build());
            } else {
                status = HttpStatus.NOT_FOUND;
                message = messageUtils.generateOrderNotFoundMessage();
                return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build());
            }
        } catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateErrorUpdateOrderMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    public ResponseEntity<Object> getOrderById(Integer orderId) {
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessGetOrderIdMessage();

        try{
            Optional<Orders> orderOptional = orderRepository.findById(orderId);
            Map<String, Object> responseData = new HashMap<>();

            if(orderOptional.isPresent()){
                Orders order = orderOptional.get();

                List<Carts> cartsList = cartsRepository.findByIsDeletedFalse();
                List<Map<String, Object>> cartsDataList = new ArrayList<>();
                for(Carts cart : cartsList) {
                    Map<String, Object> cartData = new HashMap<>();
                    cartData.put("foodName", cart.getFoods().getFoodName());
                    cartData.put("location", cart.getFoods().getLocation());
                    cartData.put("price", cart.getFoods().getPrice());
                    cartsDataList.add(cartData);
                }
                responseData.put("orderId", order.getOrderId());
                responseData.put("state", order.getState());
                responseData.put("total_item", order.getTotalItem());
                responseData.put("total_price", order.getTotalOrderPrice());
                responseData.put("detail_foods", cartsDataList);

                ResponseBodyDTO result = ResponseBodyDTO.builder()
                        .total(order.getTotalItem())
                        .data(responseData)
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build();
                return ResponseEntity.status(status).body(result);
            } else {
                status = HttpStatus.NOT_FOUND;
                message = messageUtils.generateOrderNotFoundMessage();
                return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build());
            }
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateErrorGetOrderIdMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    public ResponseEntity<Object> getOngoingOrder(Integer paxIdGsi){
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessGetOrderByPassenggerIdMessage();

        try{
            List<Orders> ordersList = orderRepository.findOngoingOrdersByPaxIdGsi(paxIdGsi);

            if(!ordersList.isEmpty()){
                List<Map<String, Object>> responseDataList = new ArrayList<>();

                for(Orders order : ordersList) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("state", order.getState());
                    responseData.put("total_item", order.getTotalItem());
                    responseData.put("total_price", order.getTotalOrderPrice());
                    responseDataList.add(responseData);
                }

                ResponseBodyDTO result = ResponseBodyDTO.builder()
                        .total(responseDataList.size())
                        .data(responseDataList)
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build();
                return ResponseEntity.status(status).body(result);
            } else {
                status = HttpStatus.NOT_FOUND;
                message = messageUtils.generateOrderNotFoundMessage();
                return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build());
            }
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateErrorGetOrderByPassenggerIdMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    public ResponseEntity<Object> getHistoricalOrders(String state, Integer paxIdGsi) {
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessGetHistoricalDataOrderMessage();

        try {
            Map<String, Object> responseData = new HashMap<>();
            List<Orders> ordersList;

            if(state != null) {
                if("ongoing".equalsIgnoreCase(state) || "complete".equalsIgnoreCase(state)) {
                    ordersList = orderRepository.findByState(state);
                } else {
                    status = HttpStatus.BAD_REQUEST;
                    message = "Invalid state parameter. It should be either 'ongoing' or 'complete'.";

                    return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                            .message(message)
                            .statusCode(status.value())
                            .status(status.name())
                            .build());
                }
            } else if (paxIdGsi != null) {
                ordersList = orderRepository.findByPaxIdGsi(paxIdGsi);
            } else {
                status = HttpStatus.BAD_REQUEST;
                message = "At least one parameter (state or paxIdGsi) is required.";
                return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                        .message(message)
                        .statusCode(status.value())
                        .status(status.name())
                        .build());
            }

            List<Map<String, Object>> ordersDataList = new ArrayList<>();

            for(Orders order : ordersList) {
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("orderId", order.getOrderId());
                orderData.put("state", order.getState());
                orderData.put("total_item", order.getTotalItem());
                orderData.put("total_price", order.getTotalOrderPrice());
                ordersDataList.add(orderData);
            }
            responseData.put("orders", ordersDataList);

            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total((long) ordersList.size())
                    .data(responseData)
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(result);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateFailedDataHistoricalDataOrderErrorMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    public ResponseEntity<Object> getStatisticsOrders(){
        HttpStatus status = HttpStatus.OK;
        String message = messageUtils.generateSuccessGetStatisticDataOrderMessage();

        try{
            Map<String, Object> responseData = new HashMap<>();
            List<Orders> ordersList = orderRepository.findAll();

            Integer totalItems = 0;
            Integer totalOrderPriceState = 0;
            Integer totalOrders = ordersList.size();
            double averageOrderPrice = 0;

            if(totalOrders > 0) {
                for(Orders order : ordersList) {
                    totalItems += order.getTotalItem();
                    totalOrderPriceState += order.getTotalOrderPrice();
                }
                averageOrderPrice = (double) totalOrderPriceState / totalOrders;
            }
            averageOrderPrice = Double.parseDouble(String.format("%.2f", averageOrderPrice));

            responseData.put("total_order", totalOrders);
            responseData.put("total_item", totalItems);
            responseData.put("total_order_price", totalOrderPriceState);
            responseData.put("averageOrderPrice", averageOrderPrice);

            ResponseBodyDTO result = ResponseBodyDTO.builder()
                    .total((long) ordersList.size())
                    .data(responseData)
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build();
            return ResponseEntity.status(status).body(result);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtils.generateFailedDataStatisticsOrderErrorMessage();
            return ResponseEntity.status(status).body(ResponseBodyDTO.builder()
                    .message(message)
                    .statusCode(status.value())
                    .status(status.name())
                    .build());
        }
    }

    private Date removeTime(Date date){
        return org.apache.commons.lang3.time.DateUtils.truncate(date, java.util.Calendar.DAY_OF_MONTH);
    }
}






















