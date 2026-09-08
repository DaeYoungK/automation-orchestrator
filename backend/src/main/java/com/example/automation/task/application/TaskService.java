package com.example.automation.task.application;

import com.example.automation.task.domain.Task;
import com.example.automation.task.dto.CreateTaskRequest;
import com.example.automation.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Long createTask(CreateTaskRequest request) {
        Task task = Task.create(request.getTaskType(), request.getTitle(), request.getDescription());

        return taskRepository.save(task).getId();
    }

    @Transactional
    public Task approveTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("해당하는 Task가 존재하지 않습니다."));

        task.approve();

        return task;
    }
}
