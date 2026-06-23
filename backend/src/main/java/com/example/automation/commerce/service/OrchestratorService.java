package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.ExecutionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrchestratorService {

    private final TaskService taskService;
    private final ExecutionService executionService;

    public Long approveAndStartTask(Long taskId, ExecutionType executionType) {
        Long executionId = taskService.approveTask(taskId, executionType);
        executionService.startExecution(executionId);

        return executionId;
    }
}
