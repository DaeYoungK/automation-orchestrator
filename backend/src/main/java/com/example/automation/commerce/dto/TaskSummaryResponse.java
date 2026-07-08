package com.example.automation.commerce.dto;

import com.example.automation.commerce.domain.TaskStatus;
import com.example.automation.commerce.domain.TaskType;

import java.time.LocalDateTime;

public record TaskSummaryResponse(
        Long taskId,
        TaskType taskType,
        TaskStatus taskStatus,
        LocalDateTime createdAt
) {
}
