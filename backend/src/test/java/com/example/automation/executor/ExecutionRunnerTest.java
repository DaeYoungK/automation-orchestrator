package com.example.automation.executor;

import com.example.automation.commerce.domain.Execution;
import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.service.ExecutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionRunnerTest {

    @Mock ExecutionExecutorFactory factory;
    @Mock ExecutionService executionService;
    @Mock ExecutionExecutor executor;
    @Mock Execution execution;
    @InjectMocks ExecutionRunner executionRunner;
    Long executionId;

    @BeforeEach
    void setUp() {
        executionId = 1L;
        when(executionService.findExecution(executionId))
                .thenReturn(this.execution);
        when(execution.getId())
                .thenReturn(executionId);
        when(execution.getExecutionType())
                .thenReturn(ExecutionType.AI);
        when(factory.getExecutor(ExecutionType.AI))
                .thenReturn(this.executor);
    }

    @Test
    void Execution_실행에_성공하면_Executor_를_호출하고_SUCCESS_처리한다() {
        //given
        
        //when
        executionRunner.run(executionId);

        //then
        verify(executionService).startExecution(executionId);
        verify(executor).execute(any(ExecutionContext.class));
        verify(executionService).successExecution(executionId);
    }
    
    @Test
    void Execution_실행에_실패하면_FAILED_처리하고_예외를_전파한다() {
        //given
        RuntimeException exception = new RuntimeException("AI 실행 실패");
        doThrow(exception)
                .when(executor)
                .execute(any(ExecutionContext.class));

        //when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> executionRunner.run(executionId));

        //then
        assertThat(thrown).isSameAs(exception);
        verify(executionService).startExecution(executionId);
        verify(executor).execute(any(ExecutionContext.class));
        verify(executionService).failExecution(executionId, "AI 실행 실패");
        verify(executionService, never()).successExecution(executionId);
    }
}