package com.example.demo.controller;


import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Object> createOrders(){
        return orderService.createOrder();
    }

    @PutMapping("/updated/{orderId}")
    public ResponseEntity<Object> updateOrder(@PathVariable(name = "orderId") Integer orderId){
        return orderService.updateOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrders(@PathVariable(name = "orderId") Integer orderId){
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/ongoing/{paxIdGsi}")
    public ResponseEntity<Object> getOngoingOrders(@PathVariable(name = "paxIdGsi") Integer paxIdGsi) {
        return orderService.getOngoingOrder(paxIdGsi);
    }

    @GetMapping("/historical")
    public ResponseEntity<Object> getHistoricalOrders(@RequestParam(name = "state", required = false) String state,@RequestParam(name = "paxIdGsi", required = false) Integer paxIdGsi){
        return orderService.getHistoricalOrders(state,paxIdGsi);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatisticsOrders(){
        return orderService.getStatisticsOrders();
    }
}
