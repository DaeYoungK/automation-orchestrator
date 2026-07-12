package com.example.automation.commerce.controller;

import com.example.automation.commerce.domain.TaskStatus;
import com.example.automation.commerce.dto.TaskSummaryResponse;
import com.example.automation.commerce.service.OrchestratorService;
import com.example.automation.commerce.service.TaskQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskQueryService taskQueryService;
    private final OrchestratorService orchestratorService;

    @GetMapping
    public List<TaskSummaryResponse> findTasks(@RequestParam(required = false) TaskStatus taskStatus) {
        return taskQueryService.findTasks(taskStatus);
    }

    @PostMapping("/{taskId}/approve")
    public void approveTask(@PathVariable(name = "taskId") Long taskId) {
        orchestratorService.approveAndCreateExecutions(taskId);
    }
}
