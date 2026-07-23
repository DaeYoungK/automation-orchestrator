package com.example.automation.executor.api;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.ExecutionExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiExecutionExecutor implements ExecutionExecutor {

    private final ApiExecutionClient apiExecutionClient;

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.API;
    }

    @Override
    public void execute(ExecutionContext executionContext) {

        ApiExecutionRequest request = new ApiExecutionRequest("urlTest", HttpMethod.POST, Map.of("test", "test"), null);
        apiExecutionClient.execute(request);
    }
}
