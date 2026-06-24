package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.*;
import com.example.automation.commerce.dto.CreateTaskRequest;
import com.example.automation.commerce.repository.ExecutionRepository;
import com.example.automation.commerce.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrchestratorServiceTest {

    @Autowired OrchestratorService orchestratorService;
    @Autowired TaskService taskService;
    @Autowired ExecutionRepository executionRepository;
    @Autowired TaskRepository taskRepository;

    @Test
    void Task_를_승인하고_실행하면_Task_는_APPROVED_이고_Execution_은_RUNNING_이다() {
        //given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest(TaskType.LOW_STOCK_REPORT, "재고 부족 리포트", "재고 부족 상품 분석");
        Long taskId = taskService.createTask(createTaskRequest);
        Task task = taskRepository.findById(taskId).orElseThrow();

        //when
        List<Long> executionIds = orchestratorService.approveAndStartTask(taskId);

        //then
        assertThat(executionIds).hasSize(1);

        Execution execution = executionRepository.findById(executionIds.get(0)).orElseThrow();

        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.APPROVED);
        assertThat(task.getExecutions()).hasSize(1);
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.RUNNING);
        assertThat(execution.getExecutionLogs()).hasSize(2);

    }
}