package com.example.automation.task.application;

import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.task.domain.Task;
import com.example.automation.task.resolver.TaskExecutionResolver;
import com.example.automation.execution.application.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrchestratorService {

    private final TaskService taskService;
    private final ExecutionService executionService;
    private final TaskExecutionResolver taskExecutionResolver;

    public List<Long> approveAndCreateExecutions(Long taskId) {
        Task task = taskService.approveTask(taskId);

        List<ExecutionType> executionTypes = taskExecutionResolver.resolve(task.getTaskType());

        List<Long> executionIds = new ArrayList<>();

        for (ExecutionType executionType : executionTypes) {
            Long executionId = executionService.createExecution(taskId, executionType);
            executionIds.add(executionId);
        }

        return executionIds;
    }
}
