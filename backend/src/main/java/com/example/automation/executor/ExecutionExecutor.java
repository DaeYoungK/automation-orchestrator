package com.example.automation.executor;

import com.example.automation.commerce.domain.ExecutionType;

public interface ExecutionExecutor {

    ExecutionType getExecutionType();

    void execute(ExecutionContext executionContext);
}
