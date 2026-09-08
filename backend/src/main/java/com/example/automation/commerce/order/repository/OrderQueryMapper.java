package com.example.automation.commerce.order.repository;

import com.example.automation.commerce.order.dto.OrderSummaryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderQueryMapper {

    List<OrderSummaryResponse> findOrderSummaries();
}
