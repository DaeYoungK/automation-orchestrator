package com.example.automation.executor.api;

import org.springframework.http.HttpMethod;

import java.util.Map;

public record ApiExecutionRequest(
        String url,
        HttpMethod httpMethod,
        Map<String, String> headers,
        Object body
) {
}
