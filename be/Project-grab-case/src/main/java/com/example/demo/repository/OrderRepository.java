package com.example.demo.repository;

import com.example.demo.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o WHERE o.paxIdGsi = :paxIdGsi AND o.state = 'ongoing'")
    List<Orders> findOngoingOrdersByPaxIdGsi(@Param("paxIdGsi") Integer paxIdGsi);

    @Query("SELECT o FROM Orders o WHERE o.state = :state")
    List<Orders> findByState(@Param("state") String state);

    List<Orders> findByPaxIdGsi(int paxIdGsi);
}























