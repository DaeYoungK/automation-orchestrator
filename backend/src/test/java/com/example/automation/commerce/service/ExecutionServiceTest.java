package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.*;
import com.example.automation.commerce.repository.ExecutionRepository;
import com.example.automation.commerce.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ExecutionServiceTest {

    @Autowired ExecutionService executionService;
    @Autowired ExecutionRepository executionRepository;
    @Autowired TaskRepository taskRepository;

    private Execution createExecutionFixture() {
        //given
        Task task = Task.create(TaskType.LOW_STOCK_REPORT, "재고부족 리포트 생성", "재고부족에 대한 리포트를 생성한다.");
        Task saveTask = taskRepository.save(task);

        //when
        Long executionId = executionService.createExecution(saveTask.getId(), ExecutionType.AI);
        return executionRepository.findById(executionId).orElseThrow();
    }

    @Test
    void Execution_을_생성하면_Task_와_연결되고_상태가_READY_이며_로그를_생성한다() {
       //given
        Task task = Task.create(TaskType.LOW_STOCK_REPORT, "재고부족 리포트 생성", "재고부족에 대한 리포트를 생성한다.");
        Task saveTask = taskRepository.save(task);

        //when
        Long executionId = executionService.createExecution(saveTask.getId(), ExecutionType.AI);
        Execution execution = executionRepository.findById(executionId).orElseThrow();

        //then
        assertThat(execution.getTask().getId()).isEqualTo(saveTask.getId());
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.READY);
        assertThat(execution.getExecutionLogs()).hasSize(1);
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getLogLevel)
                .containsExactly(LogLevel.INFO);
    }
    
    @Test
    void Execution_을_실행하면_상태가_RUNNING_이며_로그를_생성한다() {
        //given
        Execution execution = createExecutionFixture();

        //when
        executionService.startExecution(execution.getId());

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.RUNNING);
        assertThat(execution.getExecutionLogs()).hasSize(2);
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getLogLevel)
                .containsExactly(LogLevel.INFO, LogLevel.INFO);
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getMessage)
                .contains(
                        ExecutionType.AI.name() + " 작업을 실행합니다."
                );
    }

    @Test
    void Execution_가_성공하면_상태가_SUCCESS_이며_로그를_생성한다() {
        //given
        Execution execution = createExecutionFixture();
        executionService.startExecution(execution.getId());

        //when
        executionService.successExecution(execution.getId());

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.SUCCESS);
        assertThat(execution.getExecutionLogs()).hasSize(3);
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getLogLevel)
                .containsExactly(LogLevel.INFO, LogLevel.INFO ,LogLevel.INFO);
    }

    @Test
    void Execution_가_실패하면_상태가_FAILED_이며_로그를_생성한다() {
        //given
        Execution execution = createExecutionFixture();
        executionService.startExecution(execution.getId());

        //when
        executionService.failExecution(execution.getId(), "오류발생");

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.FAILED);
        assertThat(execution.getExecutionLogs()).hasSize(3);
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getLogLevel)
                .containsExactly(LogLevel.INFO, LogLevel.INFO, LogLevel.ERROR);
    }

    @Test
    void Execution_을_재시도하면_새_Execution_이_생성되고_attemptCount_가_증가하고_기존_Task_와_연결되고_상태가_READY_이며_로그를_생성한다() {
        //given
        Execution execution = createExecutionFixture();
        executionService.startExecution(execution.getId());
        executionService.failExecution(execution.getId(), "오류발생");
        Long taskId = execution.getTask().getId();

        //when
        Long retryExecutionId = executionService.createRetryExecution(execution.getId());
        Execution retryExecution = executionRepository.findById(retryExecutionId).orElseThrow();

        //then
        assertThat(retryExecution).isNotEqualTo(execution);
        assertThat(retryExecution.getAttemptCount()).isEqualTo(1);
        assertThat(retryExecution.getTask().getId()).isEqualTo(taskId);
        assertThat(retryExecution.getExecutionStatus()).isEqualTo(ExecutionStatus.READY);
        assertThat(retryExecution.getExecutionLogs()).hasSize(1);
        assertThat(execution.getExecutionLogs()).hasSize(4);
        assertThat(retryExecution.getExecutionLogs())
                .extracting(ExecutionLog::getMessage)
                .contains(ExecutionType.AI.name() + " 재시도 작업이 생성되었습니다.");
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getMessage)
                .contains(ExecutionType.AI.name() + " 재시도 작업을 생성합니다.");
    }

    @Test
    void Execution_를_취소하면_상태가_CANCELED_이며_로그를_생성한다() {
        //given
        Execution execution = createExecutionFixture();
        executionService.startExecution(execution.getId());

        //when
        executionService.cancelExecution(execution.getId());

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.CANCELED);
        assertThat(execution.getExecutionLogs()).hasSize(3);
    }

    @Test
    void Task_와_연결되지_않은_Execution_은_재시도_불가능하다() {
        //given && //when
        Execution execution = Execution.create(ExecutionType.AI);
        Execution savedExecution = executionRepository.save(execution);

        //then
        assertThatThrownBy(() -> executionService.createRetryExecution(savedExecution.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Task에 연결된 Execution만 재시도할 수 있습니다.");
    }
}