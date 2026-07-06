package com.example.automation.commerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scheduler")
public record SchedulerProperties(LowStock lowStock) {
    public record LowStock(
            String cron
    ){

    }
}
