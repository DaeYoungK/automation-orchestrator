package com.example.automation.executor.api.resolver;

import com.example.automation.commerce.domain.Execution;
import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.domain.TaskType;
import com.example.automation.commerce.service.ExecutionService;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.api.ApiExecutionRequest;
import com.example.automation.executor.api.provider.ApiExecutionRequestProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ApiExecutionRequestResolver {

    private final ExecutionService executionService;
    private final Map<TaskType, ApiExecutionRequestProvider> providerMap;

    public ApiExecutionRequestResolver(
            ExecutionService executionService,
            List<ApiExecutionRequestProvider> providers
    ) {
        this.executionService = executionService;
        this.providerMap = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ApiExecutionRequestProvider::getTaskType,
                        Function.identity()
                ));
    }

    public ApiExecutionRequest resolve(ExecutionContext executionContext) {

        Execution execution = executionService.findExecution(executionContext.executionId());
        Task task = execution.getTask();
        TaskType taskType = task.getTaskType();

        ApiExecutionRequestProvider provider = providerMap.get(taskType);
        if (provider == null) {
            throw new IllegalArgumentException(taskType + "에 대응하는 provider가 존재하지 않습니다.");
        }

        return provider.create(task);
    }
}
