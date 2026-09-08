package com.example.automation.commerce.order.domain;

public enum OrderStatus {

    CREATED("주문생성"),
    PAID("결제완료"),
    COMPLETED("주문완료"),
    CANCELED("주문취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
    
    //isCancelable() 같은 정책 들어갈수 있음
}
