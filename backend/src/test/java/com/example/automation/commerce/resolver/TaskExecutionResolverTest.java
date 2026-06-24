package com.example.automation.commerce.resolver;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.domain.TaskType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TaskExecutionResolverTest {

    TaskExecutionResolver taskExecutionResolver = new TaskExecutionResolver();

    @Test
    void TaskType_를_매개변수로_넣으면_설정된_ExecutionType_으로_변환된다() {
        //given
        TaskType taskType1 = TaskType.LOW_STOCK_REPORT;
        TaskType taskType2 = TaskType.PURCHASE_REQUEST_REVIEW;
        TaskType taskType3 = TaskType.RPA_FAILURE_RECOVERY;

        //when
        List<ExecutionType> executionTypes1 = taskExecutionResolver.resolve(taskType1);
        ExecutionType executionType1 = executionTypes1.get(0);

        List<ExecutionType> executionTypes2 = taskExecutionResolver.resolve(taskType2);
        ExecutionType executionType2 = executionTypes2.get(0);

        List<ExecutionType> executionTypes3 = taskExecutionResolver.resolve(taskType3);
        ExecutionType executionType3 = executionTypes3.get(0);

        //then
        assertThat(executionType1).isEqualTo(ExecutionType.AI);
        assertThat(executionTypes1).hasSize(1);
        assertThat(executionType2).isEqualTo(ExecutionType.EMAIL);
        assertThat(executionTypes2).hasSize(1);
        assertThat(executionType3).isEqualTo(ExecutionType.RPA);
        assertThat(executionTypes3).hasSize(1);
    }

}