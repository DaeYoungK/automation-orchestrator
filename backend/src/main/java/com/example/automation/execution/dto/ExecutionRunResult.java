package com.example.automation.execution.dto;

import com.example.automation.execution.domain.ExecutionStatus;

public record ExecutionRunResult(
        Long executionId,
        ExecutionStatus executionStatus,
        boolean retryable,
        String failureMessage
) {
}
