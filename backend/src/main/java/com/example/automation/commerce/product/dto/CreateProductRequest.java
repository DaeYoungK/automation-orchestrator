package com.example.automation.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private int price;
    private int quantity;
}
