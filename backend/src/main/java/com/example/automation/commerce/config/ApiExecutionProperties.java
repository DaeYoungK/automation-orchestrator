package com.example.automation.commerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "execution.api")
public record ApiExecutionProperties(
        String baseUrl
) {
}
