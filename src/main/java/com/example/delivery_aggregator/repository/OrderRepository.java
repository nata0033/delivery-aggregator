package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.Order;
import com.example.delivery_aggregator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUserOrderByCreatedAtDesc(User user);
}
