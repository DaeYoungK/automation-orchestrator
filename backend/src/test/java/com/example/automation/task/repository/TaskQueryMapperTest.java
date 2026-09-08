package com.example.automation.task.repository;

import com.example.automation.task.domain.Task;
import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.dto.TaskSummaryResponse;
import com.example.automation.task.fixture.TaskFixture;
import com.example.automation.task.repository.TaskQueryMapper;
import com.example.automation.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskQueryMapperTest {

    @Autowired
    TaskQueryMapper taskQueryMapper;
    @Autowired
    TaskRepository taskRepository;
    Task pendingTask;
    Task approveTask;
    Task failedTask;


    @BeforeEach
    void createTasks() {
        List<Task> tasks = new ArrayList<>();
        pendingTask = TaskFixture.pendingTask();
        approveTask = TaskFixture.approvedTask();
        failedTask = TaskFixture.failedTask();
        tasks.add(pendingTask);
        tasks.add(approveTask);
        tasks.add(failedTask);

        taskRepository.saveAll(tasks);
        taskRepository.flush();
    }

    @Test
    void Task_를_조회하면_모든_Task_가_조회된다() {
        //given
        //when
        List<TaskSummaryResponse> tasks = taskQueryMapper.findTasks(null);

        //then
        assertThat(tasks).hasSize(3);
    }

    @Test
    void Task_를_상태와함께_조회하면_해당하는_Task_가_조회된다() {
        //given
        //when
        List<TaskSummaryResponse> pendingTasks = taskQueryMapper.findTasks(TaskStatus.PENDING);
        List<TaskSummaryResponse> approvedTasks = taskQueryMapper.findTasks(TaskStatus.APPROVED);

        //then
        assertThat(pendingTasks)
                .hasSize(1)
                .allMatch(task -> task.taskStatus() == TaskStatus.PENDING);
        assertThat(approvedTasks)
                .hasSize(1)
                .allMatch(task -> task.taskStatus() == TaskStatus.APPROVED);
    }
}