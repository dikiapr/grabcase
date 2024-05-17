package com.example.demo.kafka;

import com.example.demo.model.Orders;
import com.example.demo.model.OrdersMongoDB;
import com.example.demo.repository.OrderMongoDBRepository;
import com.example.demo.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderKafkaConsumer.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMongoDBRepository orderMongoDBRepository;

    public void consumerOrder(Orders order){
        LOGGER.info(String.format("Order received: %s", order));

        orderRepository.save(order);
        LOGGER.info("Order saved to PostgreSQL database");

        OrdersMongoDB ordersMongoDB = convertToMongoDBOrder(order);
        orderMongoDBRepository.save(ordersMongoDB);
        LOGGER.info("Order saved to MongoDB database");
    }

    private OrdersMongoDB convertToMongoDBOrder(Orders order){
        return OrdersMongoDB.builder()
                .orderId(order.getOrderId())
                .paxIdGsi(order.getPaxIdGsi())
                .orderDate(order.getOrderDate())
                .createdBy(order.getCreatedBy())
                .modifiedBy(order.getModifiedBy())
                .paxId(order.getPaxId())
                .state(order.getState())
                .totalItem(order.getTotalItem())
                .totalOrderPrice(order.getTotalOrderPrice())
                .build();
    }

    @KafkaListener(topics = "order-topic", groupId = "myGroup")
    public void consumeUpdatedOrder(Orders order){
        LOGGER.info(String.format("Updated order received: %s", order));

        updateOrderState(order.getOrderId(), "complete");

        LOGGER.info("Order status updated in PostgreSQL and MongoDB databases");
    }

    private void updateOrderState(Integer orderId, String newState){

        Orders orderFromPostgres = orderRepository.findById(orderId).orElse(null);
        if(orderFromPostgres != null){
            orderFromPostgres.setState(newState);
            orderRepository.save(orderFromPostgres);
        }

        OrdersMongoDB orderFromMongoDB = orderMongoDBRepository.findByOrderId(orderId);
        if(orderFromMongoDB != null){
            orderFromMongoDB.setState(newState);
            orderMongoDBRepository.save(orderFromMongoDB);
        }
    }
}



























