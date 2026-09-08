package com.example.automation.executor.api.provider;

import com.example.automation.task.domain.Task;
import com.example.automation.task.domain.TaskType;
import com.example.automation.executor.api.ApiExecutionRequest;

public interface ApiExecutionRequestProvider {

    TaskType getTaskType();

    ApiExecutionRequest create(Task task);
}
