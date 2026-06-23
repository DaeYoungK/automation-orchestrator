package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.dto.CreateTaskRequest;
import com.example.automation.commerce.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ExecutionService executionService;

    @Transactional
    public Long createTask(CreateTaskRequest request) {
        Task task = Task.create(request.getTaskType(), request.getTitle(), request.getDescription());

        return taskRepository.save(task).getId();
    }

    @Transactional
    public Long approveTask(Long taskId, ExecutionType executionType) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("해당하는 Task가 존재하지 않습니다."));
        task.approve();

        executionService.createExecution(taskId, executionType);

        return taskId;
    }
}
