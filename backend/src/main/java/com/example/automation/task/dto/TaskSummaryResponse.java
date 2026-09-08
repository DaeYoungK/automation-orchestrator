package com.example.automation.task.dto;

import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.domain.TaskType;

import java.time.LocalDateTime;

public record TaskSummaryResponse(
        Long taskId,
        TaskType taskType,
        TaskStatus taskStatus,
        LocalDateTime createdAt
) {
}
