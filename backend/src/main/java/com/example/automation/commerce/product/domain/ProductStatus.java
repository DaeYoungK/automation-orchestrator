package com.example.automation.commerce.product.domain;

import lombok.Getter;

@Getter
public enum ProductStatus {
    PREPARATION("상품준비중"),
    ON_SALE("판매중"),
    SOLD_OUT("품절");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}
