package com.example.demo.repository;

import com.example.demo.model.OrdersMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoDBRepository extends MongoRepository<OrdersMongoDB, String> {

    OrdersMongoDB findByOrderId(int orderId);
}
