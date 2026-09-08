package com.example.automation.task.application;

import com.example.automation.task.dto.CreateTaskRequest;
import com.example.automation.execution.repository.ExecutionRepository;
import com.example.automation.task.repository.TaskRepository;
import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.domain.ExecutionStatus;
import com.example.automation.task.application.OrchestratorService;
import com.example.automation.task.application.TaskService;
import com.example.automation.task.domain.Task;
import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.domain.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrchestratorServiceTest {

    @Autowired
    OrchestratorService orchestratorService;
    @Autowired
    TaskService taskService;
    @Autowired ExecutionRepository executionRepository;
    @Autowired TaskRepository taskRepository;

    @Test
    void Task_를_승인하면_Execution_은_생성되고_Task_는_APPROVED_이며_Execution_은_READY_이다() {
        //given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest(TaskType.LOW_STOCK_REPORT, "재고 부족 리포트", "재고 부족 상품 분석");
        Long taskId = taskService.createTask(createTaskRequest);
        Task task = taskRepository.findById(taskId).orElseThrow();

        //when
        List<Long> executionIds = orchestratorService.approveAndCreateExecutions(taskId);

        //then
        assertThat(executionIds).hasSize(1);

        Execution execution = executionRepository.findById(executionIds.get(0)).orElseThrow();

        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.APPROVED);
        assertThat(task.getExecutions()).hasSize(1);
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.READY);
        assertThat(execution.getExecutionLogs()).hasSize(1);

    }
}