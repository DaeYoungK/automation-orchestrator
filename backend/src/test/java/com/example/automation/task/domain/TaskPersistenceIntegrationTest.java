package com.example.automation.task.domain;

import com.example.automation.task.repository.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskPersistenceIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void Task_를_저장하면_TaskInput_도_함께_저장된다() {
        //given
        Task task = new Task(TaskType.LOW_STOCK_REPORT, "재고부족 리포트", "재고부족 리포트를 작성한다.");
        TaskInput taskInput = TaskInput.createFile("key", 1L);
        task.addTaskInput(taskInput);

        //when
        Task savedTask = taskRepository.save(task);
        em.flush();
        em.clear();

        Task foundTask = taskRepository.findById(savedTask.getId()).orElseThrow();

        //then
        assertThat(foundTask.getTaskInputs()).hasSize(1);

        TaskInput foundInput = foundTask.getTaskInputs().get(0);
        assertThat(foundInput.getKey()).isEqualTo("key");
        assertThat(foundInput.getStoredFileId()).isEqualTo(1L);
        assertThat(foundInput.getTaskInputType()).isEqualTo(TaskInputType.FILE);
    }

}
