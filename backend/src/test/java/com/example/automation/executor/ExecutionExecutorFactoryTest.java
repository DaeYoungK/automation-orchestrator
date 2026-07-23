package com.example.automation.executor;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.executor.ai.AiExecutionExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ExecutionExecutorFactoryTest {

    @Autowired ExecutionExecutorFactory executionExecutorFactory;

    @ParameterizedTest
    @EnumSource(ExecutionType.class)
    void ExecutionType에_맞는_Executor를_반환한다(ExecutionType executionType) {
        //when
        ExecutionExecutor executor = executionExecutorFactory.getExecutor(executionType);

        //then
        assertThat(executor.getExecutionType()).isEqualTo(executionType);
    }

    @Test
    void ExecutionType_이_AI_타입이면_AiExecutionExecutor_를_반환한다() {
        //when
        ExecutionExecutor executor = executionExecutorFactory.getExecutor(ExecutionType.AI);

        //then
        assertThat(executor)
                .isInstanceOf(AiExecutionExecutor.class);
    }

    @Test
    void ExecutionType_이_null_이면_예외가_발생한다() {
        assertThatThrownBy(() ->
                executionExecutorFactory.getExecutor(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ExecutionType은 null일 수 없습니다.");
    }

    @Test
    void 등록되지_않은_ExecutionType_이면_예외가_발생한다() {
        //given && when
        ExecutionExecutorFactory factory = new ExecutionExecutorFactory(List.of());

        //then
        assertThatThrownBy(() -> factory.getExecutor(ExecutionType.RPA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RPA 타입의 Executor가 존재하지 않습니다.");
    }
}