package com.example.automation.executor;

import com.example.automation.commerce.domain.ExecutionType;

public record ExecutionContext (
        Long executionId,
        ExecutionType executionType
){
}
