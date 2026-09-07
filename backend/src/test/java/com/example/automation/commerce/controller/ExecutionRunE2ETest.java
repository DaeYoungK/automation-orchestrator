package com.example.automation.commerce.controller;

import com.example.automation.commerce.domain.*;
import com.example.automation.commerce.repository.ExecutionRepository;
import com.example.automation.commerce.repository.NotificationRepository;
import com.example.automation.commerce.repository.TaskRepository;
import com.example.automation.executor.dto.ExecutionRunResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=18080",
                "execution.api.base-url=http://localhost:18080"
        }
)
class ExecutionRunE2ETest {

    @Autowired ExecutionRepository executionRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired NotificationRepository notificationRepository;
    private RestClient client;
    private Task task;
    private Execution execution;

    @BeforeEach
    void init() {
        task = Task.create(TaskType.LOW_STOCK_REPORT, "재고 부족", "재고 부족 테스트");
        execution = Execution.create(ExecutionType.API);

        task.addExecution(execution);
        taskRepository.save(task);

        client = RestClient.builder().baseUrl("http://localhost:18080").build();
    }

    @AfterEach
    void cleanUp() {
        notificationRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    void LOW_STOCK_REPORT_API_Execution을_실행하면_Notification이_저장되고_SUCCESS가_된다() {
        //given
        //when
        ExecutionRunResponse response = client.post()
                .uri("/api/executions/{executionId}/run", execution.getId())
                        .retrieve()
                                .body(ExecutionRunResponse.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.executionId()).isEqualTo(execution.getId());
        assertThat(response.executionStatus()).isEqualTo(ExecutionStatus.SUCCESS);
        assertThat(response.retryable()).isFalse();
        assertThat(response.failureMessage()).isNull();

        Execution savedExecution = executionRepository.findById(execution.getId())
                .orElseThrow();

        assertThat(savedExecution.getExecutionStatus())
                .isEqualTo(ExecutionStatus.SUCCESS);

        Notification notification = notificationRepository.findByTaskId(task.getId())
                .orElseThrow();

        assertThat(notification.getTaskId()).isEqualTo(task.getId());
        assertThat(notification.getTitle()).isEqualTo(task.getTitle());
        assertThat(notification.getDescription()).isEqualTo(task.getDescription());
    }
}
