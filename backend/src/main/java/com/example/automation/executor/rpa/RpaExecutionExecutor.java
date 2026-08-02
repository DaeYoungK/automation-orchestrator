package com.example.automation.executor.rpa;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import org.springframework.stereotype.Component;

@Component
public class RpaExecutionExecutor implements ExecutionExecutor {
    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.RPA;
    }

    @Override
    public void execute(ExecutionContext executionContext) {
        // TODO: 2026-07-13
        throw new UnsupportedOperationException("RPA ExecutionExecutor는 아직 구현되지 않았습니다.");
    }
}
