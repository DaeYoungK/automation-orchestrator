package com.example.automation.execution.controller;

import com.example.automation.execution.application.ExecutionRunService;
import com.example.automation.execution.dto.ExecutionRunResponse;
import com.example.automation.execution.dto.ExecutionRunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/executions")
public class ExecutionController {

    private final ExecutionRunService executionRunService;

    @PostMapping("/{executionId}/run")
    public ResponseEntity<ExecutionRunResponse> runExecution(
            @PathVariable(name = "executionId") Long executionId
    ) {
        ExecutionRunResult result = executionRunService.runExecution(executionId);
        ExecutionRunResponse response = ExecutionRunResponse.from(result);

        return ResponseEntity.ok(response);
    }
}
