package com.example.automation.executor.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "execution.api")
public record ApiExecutionProperties(
        String baseUrl
) {
}
