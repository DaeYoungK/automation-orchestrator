package com.example.automation.commerce.repository;

import com.example.automation.commerce.dto.OrderSummaryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderQueryMapper {

    List<OrderSummaryResponse> findOrderSummaries();
}
