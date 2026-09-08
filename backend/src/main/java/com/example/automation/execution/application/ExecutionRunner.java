package com.example.automation.execution.application;

import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.application.ExecutionService;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import com.example.automation.executor.ExecutionExecutorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionRunner {

    private final ExecutionService executionService;
    private final ExecutionExecutorFactory factory;

    public void run(Long executionId) {
        Execution execution = executionService.findExecution(executionId);

        ExecutionContext executionContext = new ExecutionContext(execution.getId(), execution.getExecutionType());
        ExecutionExecutor executor = factory.getExecutor(execution.getExecutionType());

        executionService.startExecution(execution.getId());

        try {
            executor.execute(executionContext);
            executionService.successExecution(execution.getId());
        } catch (RuntimeException e) {
            executionService.failExecution(execution.getId(), e.getMessage());
            throw e;
        }
    }
}
