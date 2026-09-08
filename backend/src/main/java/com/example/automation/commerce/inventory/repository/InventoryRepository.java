package com.example.automation.commerce.inventory.repository;

import com.example.automation.commerce.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
