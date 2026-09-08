package com.example.automation.executor.api;

import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import com.example.automation.executor.api.resolver.ApiExecutionRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiExecutionExecutor implements ExecutionExecutor {

    private final ApiExecutionClient apiExecutionClient;
    private final ApiExecutionRequestResolver resolver;

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.API;
    }

    @Override
    public void execute(ExecutionContext executionContext) {

        ApiExecutionRequest request = resolver.resolve(executionContext);
        apiExecutionClient.execute(request);
    }
}
