package com.example.automation.executor.api.provider;

import com.example.automation.commerce.config.ApiExecutionProperties;
import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.domain.TaskType;
import com.example.automation.commerce.dto.NotificationRequest;
import com.example.automation.executor.api.ApiExecutionRequest;
import com.example.automation.executor.api.url.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LowStockNotificationApiRequestProvider implements ApiExecutionRequestProvider {

    private final ApiExecutionProperties apiExecutionProperties;

    @Override
    public TaskType getTaskType() {
        return TaskType.LOW_STOCK_REPORT;
    }

    @Override
    public ApiExecutionRequest create(Task task) {
        NotificationRequest requestBody = new NotificationRequest(task.getId(), task.getTitle(), task.getDescription());

        return new ApiExecutionRequest(
                apiExecutionProperties.baseUrl() + ApiEndpoints.NOTIFICATIONS,
                HttpMethod.POST,
                Map.of("Content-Type", "application/json"),
                requestBody
        );
    }
}
