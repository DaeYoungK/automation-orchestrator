package com.example.automation.commerce.dto;

import com.example.automation.commerce.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private TaskType taskType;
    private String title;
    private String description;
}
