package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.TaskStatus;
import com.example.automation.commerce.dto.TaskSummaryResponse;
import com.example.automation.commerce.repository.TaskQueryMapper;
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
