package com.example.automation.executor.dto;

import com.example.automation.commerce.domain.ExecutionStatus;

public record ExecutionRunResult(
        Long executionId,
        ExecutionStatus executionStatus,
        boolean retryable,
        String failureMessage
) {
}
