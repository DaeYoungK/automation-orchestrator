package com.example.automation.task.controller;

import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.dto.TaskSummaryResponse;
import com.example.automation.task.application.OrchestratorService;
import com.example.automation.task.application.TaskQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
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
