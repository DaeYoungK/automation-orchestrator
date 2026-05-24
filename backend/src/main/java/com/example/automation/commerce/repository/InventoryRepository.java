package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
