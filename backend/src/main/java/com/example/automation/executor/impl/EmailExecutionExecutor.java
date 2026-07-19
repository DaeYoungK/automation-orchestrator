package com.example.automation.executor.impl;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import org.springframework.stereotype.Component;

@Component
public class EmailExecutionExecutor implements ExecutionExecutor {
    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.EMAIL;
    }

    @Override
    public void execute(ExecutionContext executionContext) {
        // TODO: 2026-07-13
        throw new UnsupportedOperationException("Email ExecutionExecutor는 아직 구현되지 않았습니다.");
    }
}
