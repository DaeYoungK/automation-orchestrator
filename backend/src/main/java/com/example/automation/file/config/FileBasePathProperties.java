package com.example.automation.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record FileBasePathProperties(
        String basePath
) {
}
