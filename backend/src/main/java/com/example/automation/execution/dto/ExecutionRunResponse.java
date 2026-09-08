package com.example.automation.execution.dto;

import com.example.automation.execution.domain.ExecutionStatus;

public record ExecutionRunResponse(
        Long executionId,
        ExecutionStatus executionStatus,
        boolean retryable,
        String failureMessage
) {
    public static ExecutionRunResponse from(ExecutionRunResult result) {
        return new ExecutionRunResponse(
                result.executionId(),
                result.executionStatus(),
                result.retryable(),
                result.failureMessage()
        );
    }
}
