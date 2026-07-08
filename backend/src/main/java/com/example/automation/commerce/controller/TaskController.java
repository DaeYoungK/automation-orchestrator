package com.example.automation.commerce.controller;

import com.example.automation.commerce.domain.TaskStatus;
import com.example.automation.commerce.dto.TaskSummaryResponse;
import com.example.automation.commerce.service.TaskQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskQueryService taskQueryService;

    @GetMapping
    public List<TaskSummaryResponse> findTasks(@RequestParam(required = false) TaskStatus taskStatus) {
        return taskQueryService.findTasks(taskStatus);
    }
}
