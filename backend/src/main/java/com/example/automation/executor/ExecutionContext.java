package com.example.automation.executor;

import com.example.automation.execution.domain.ExecutionType;

public record ExecutionContext (
        Long executionId,
        ExecutionType executionType
){
}
