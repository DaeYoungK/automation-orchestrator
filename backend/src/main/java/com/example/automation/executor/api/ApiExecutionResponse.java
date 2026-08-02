package com.example.automation.executor.api;

public record ApiExecutionResponse(
        int statusCode,
        String body
) {
}
