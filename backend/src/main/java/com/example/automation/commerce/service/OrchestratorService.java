package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.repository.TaskRepository;
import com.example.automation.commerce.resolver.TaskExecutionResolver;
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
    private final TaskRepository taskRepository;
    private final TaskExecutionResolver taskExecutionResolver;

    public List<Long> approveAndStartTask(Long taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task가 존재하지 않습니다."));

        List<ExecutionType> executionTypes = taskExecutionResolver.resolve(task.getTaskType());

        taskService.approveTask(taskId);

        List<Long> executionIds = new ArrayList<>();

        for (ExecutionType executionType : executionTypes) {
            Long executionId = executionService.createExecution(taskId, executionType);
            executionService.startExecution(executionId);
            executionIds.add(executionId);
        }

        return executionIds;
    }
}
