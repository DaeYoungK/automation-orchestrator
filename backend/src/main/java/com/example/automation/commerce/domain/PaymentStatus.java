package com.example.automation.commerce.domain;

public enum PaymentStatus {
    PENDING("결제대기"),
    SUCCESS("결제성공"),
    FAILED("결제실패"),
    CANCELED("결제취소");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
