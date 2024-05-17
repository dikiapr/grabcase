package com.example.demo.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders_mongodb")
public class OrdersMongoDB {

    @Id
    private String id;

    private int orderId;

    private int paxIdGsi;

    private Date orderDate;

    private String createdBy;

    private String modifiedBy;

    private String paxId;

    private String state;

    private Integer totalItem;

    private Integer totalOrderPrice;
}





















