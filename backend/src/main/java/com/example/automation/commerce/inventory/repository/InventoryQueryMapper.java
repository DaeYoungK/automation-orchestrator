package com.example.automation.commerce.inventory.repository;

import com.example.automation.task.dto.LowStockProductResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InventoryQueryMapper {

    List<LowStockProductResponse> findLowStockProducts(long threshold);
}
