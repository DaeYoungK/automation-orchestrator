package com.example.automation.commerce.product.repository;

import com.example.automation.commerce.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
