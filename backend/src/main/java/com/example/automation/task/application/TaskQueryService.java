package com.example.automation.task.application;

import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.dto.TaskSummaryResponse;
import com.example.automation.task.repository.TaskQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskQueryService {

    private final TaskQueryMapper taskQueryMapper;

    public List<TaskSummaryResponse> findTasks(TaskStatus taskStatus) {
        return taskQueryMapper.findTasks(taskStatus);
    }
}
