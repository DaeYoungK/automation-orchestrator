package com.example.automation.commerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "automation")
public record LowStockProperties(LowStock lowStock) {
    public record LowStock(long threshold) {

    }
}
