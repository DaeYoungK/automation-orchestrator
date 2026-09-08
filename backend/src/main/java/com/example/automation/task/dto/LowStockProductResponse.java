package com.example.automation.task.dto;

import com.example.automation.commerce.product.domain.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LowStockProductResponse {

    private Long productId;
    private Long inventoryId;
    private String productName;
    private long availableQuantity;
    private ProductStatus productStatus;
}
