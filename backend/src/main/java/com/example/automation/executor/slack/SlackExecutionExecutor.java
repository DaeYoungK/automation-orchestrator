package com.example.automation.executor.slack;

import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import org.springframework.stereotype.Component;

@Component
public class SlackExecutionExecutor implements ExecutionExecutor {
    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.SLACK;
    }

    @Override
    public void execute(ExecutionContext executionContext) {
        // TODO: 2026-07-13
        throw new UnsupportedOperationException("Slack ExecutionExecutor는 아직 구현되지 않았습니다.");
    }
}
