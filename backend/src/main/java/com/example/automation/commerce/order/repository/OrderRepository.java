package com.example.automation.commerce.order.repository;

import com.example.automation.commerce.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
