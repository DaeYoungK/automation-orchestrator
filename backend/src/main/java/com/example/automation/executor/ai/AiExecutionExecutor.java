package com.example.automation.executor.ai;

import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import org.springframework.stereotype.Component;

@Component
public class AiExecutionExecutor implements ExecutionExecutor {
    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.AI;
    }

    @Override
    public void execute(ExecutionContext executionContext) {
        // TODO: 2026-07-13
        throw new UnsupportedOperationException("AI ExecutionExecutor는 아직 구현되지 않았습니다.");
    }
}
