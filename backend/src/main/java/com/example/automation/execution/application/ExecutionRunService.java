package com.example.automation.execution.application;

import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.domain.ExecutionStatus;
import com.example.automation.execution.dto.ExecutionRunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionRunService {

    private final ExecutionRunner executionRunner;
    private final ExecutionService executionService;

    public ExecutionRunResult runExecution(Long executionId) {

        try {
            executionRunner.run(executionId);
        } catch (RuntimeException e) {
            Execution failedExecution = executionService.findExecution(executionId);

            if (failedExecution.getExecutionStatus() != ExecutionStatus.FAILED) {
                throw e;
            }

            return new ExecutionRunResult(
                    failedExecution.getId(),
                    failedExecution.getExecutionStatus(),
                    failedExecution.isRetryable(),
                    failedExecution.getErrorMessage()
            );
        }
        Execution successExecution = executionService.findExecution(executionId);

        if (successExecution.getExecutionStatus() != ExecutionStatus.SUCCESS) {
            throw new IllegalStateException("ExecutionStatus가 SUCCESS가 아닙니다.");
        }

        return new ExecutionRunResult(
                successExecution.getId(),
                successExecution.getExecutionStatus(),
                false,
                null
        );
    }
}
