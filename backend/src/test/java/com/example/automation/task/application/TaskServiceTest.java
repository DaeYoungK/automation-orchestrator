package com.example.automation.task.application;

import com.example.automation.task.dto.CreateTaskRequest;
import com.example.automation.task.repository.TaskRepository;
import com.example.automation.task.application.TaskService;
import com.example.automation.task.domain.Task;
import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.domain.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {
    
    @Autowired
    TaskService taskService;
    @Autowired TaskRepository taskRepository;

    private CreateTaskRequest createTaskRequest() {
        return new CreateTaskRequest(TaskType.LOW_STOCK_REPORT, "재고 부족 리포트", "재고 부족 상품 분석");
    }

    @Test
    void Task_를_생성하면_상태가_PENDING_이다() {
        //given
        CreateTaskRequest createTaskRequest = createTaskRequest();

        //when
        Long taskId = taskService.createTask(createTaskRequest);
        Task task = taskRepository.findById(taskId).orElseThrow();

        //then
        assertThat(task.getTaskType()).isEqualTo(TaskType.LOW_STOCK_REPORT);
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.PENDING);
    }
    
    @Test
    void Task_를_승인하면_상태는_APPROVED_이다() {
        //given
        CreateTaskRequest createTaskRequest = createTaskRequest();
        Long taskId = taskService.createTask(createTaskRequest);
        Task task = taskRepository.findById(taskId).orElseThrow();

        //when
        taskService.approveTask(taskId);
        
        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.APPROVED);
    }
}