package com.microservicedemo.orderservice.repository;

import com.microservicedemo.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
