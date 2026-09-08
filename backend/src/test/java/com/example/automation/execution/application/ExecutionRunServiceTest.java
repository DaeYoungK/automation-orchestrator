package com.example.automation.execution.application;

import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.domain.ExecutionStatus;
import com.example.automation.execution.dto.ExecutionRunResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ExecutionRunServiceTest {

    @Mock
    ExecutionService executionService;
    @Mock ExecutionRunner executionRunner;
    @Mock Execution execution;
    @InjectMocks
    ExecutionRunService executionRunService;
    Long executionId;

    @BeforeEach
    void setUp() {
        executionId = 1L;
        when(executionService.findExecution(executionId))
                .thenReturn(this.execution);
    }

    @Test
    void Runner_가_정상종료하면_SUCCESS_를_반환한다() {
        //given
        when(execution.getId())
                .thenReturn(executionId);
        when(execution.getExecutionStatus())
                .thenReturn(ExecutionStatus.SUCCESS);

        //when
        ExecutionRunResult result = executionRunService.runExecution(executionId);

        //then
        verify(executionRunner).run(executionId);
        assertThat(result.executionId()).isEqualTo(executionId);
        assertThat(result.executionStatus()).isEqualTo(ExecutionStatus.SUCCESS);
        assertThat(result.retryable()).isFalse();
        assertThat(result.failureMessage()).isNull();
    }

    @Test
    void Runner_가_예외를_발생하면_FAILED_를_반환한다() {
        //given
        String failedMessage = "예외발생";
        RuntimeException exception = new RuntimeException(failedMessage);
        when(execution.getId())
                .thenReturn(executionId);
        when(execution.getExecutionStatus())
                .thenReturn(ExecutionStatus.FAILED);
        when(execution.getErrorMessage())
                .thenReturn(failedMessage);
        when(execution.isRetryable())
                .thenReturn(true);
        doThrow(exception)
                .when(executionRunner).run(executionId);

        //when
        ExecutionRunResult result = executionRunService.runExecution(executionId);

        //then
        assertThat(result.executionId()).isEqualTo(executionId);
        assertThat(result.executionStatus()).isEqualTo(ExecutionStatus.FAILED);
        assertThat(result.retryable()).isTrue();
        assertThat(result.failureMessage()).isEqualTo(failedMessage);
    }

    @Test
    void FAILED_이고_재시도할_수_없으면_retryable_false_를_반환한다() {
        //given
        String failedMessage = "예외발생";
        RuntimeException exception = new RuntimeException(failedMessage);
        when(execution.getId())
                .thenReturn(executionId);
        when(execution.getExecutionStatus())
                .thenReturn(ExecutionStatus.FAILED);
        when(execution.getErrorMessage())
                .thenReturn(failedMessage);
        when(execution.isRetryable())
                .thenReturn(false);
        doThrow(exception)
                .when(executionRunner).run(executionId);

        //when
        ExecutionRunResult result = executionRunService.runExecution(executionId);

        //then
        assertThat(result.executionId()).isEqualTo(executionId);
        assertThat(result.executionStatus()).isEqualTo(ExecutionStatus.FAILED);
        assertThat(result.retryable()).isFalse();
        assertThat(result.failureMessage()).isEqualTo(failedMessage);
    }

    @Test
    void Runner_정상종료후_SUCCESS_가_아니면_예외가_발생한다() {
        //given
        when(execution.getExecutionStatus())
                .thenReturn(ExecutionStatus.READY);


        // when & then
        assertThatThrownBy(() -> executionRunService.runExecution(executionId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ExecutionStatus가 SUCCESS가 아닙니다.");
    }

    @Test
    void Runner_실패후_FAILED_가_아니면_예외가_발생한다() {
        //given
        RuntimeException exception = new RuntimeException();
        when(execution.getExecutionStatus())
                .thenReturn(ExecutionStatus.READY);
        doThrow(exception)
                .when(executionRunner).run(executionId);

        // when & then
        assertThatThrownBy(() -> executionRunService.runExecution(executionId))
                .isSameAs(exception);
    }
}