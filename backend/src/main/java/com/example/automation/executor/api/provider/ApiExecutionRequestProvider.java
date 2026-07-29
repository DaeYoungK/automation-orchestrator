package com.example.automation.executor.api.provider;

import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.domain.TaskType;
import com.example.automation.executor.api.ApiExecutionRequest;

public interface ApiExecutionRequestProvider {

    TaskType getTaskType();

    ApiExecutionRequest create(Task task);
}
