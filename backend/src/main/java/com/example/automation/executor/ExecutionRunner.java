package com.example.automation.executor;

import com.example.automation.commerce.domain.Execution;
import com.example.automation.commerce.service.ExecutionService;
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
