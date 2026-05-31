package com.example.automation.commerce.dto;

import com.example.automation.commerce.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderSummaryResponse {

    private Long orderId;
    private String address;
    private OrderStatus orderStatus;
    private int totalPrice;
    private LocalDateTime createdAt;
}
