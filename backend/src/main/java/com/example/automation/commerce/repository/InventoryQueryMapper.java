package com.example.automation.commerce.repository;

import com.example.automation.commerce.dto.LowStockProductResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InventoryQueryMapper {

    List<LowStockProductResponse> findLowStockProducts(long threshold);
}
