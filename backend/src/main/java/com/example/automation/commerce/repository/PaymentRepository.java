package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
