package com.example.automation.executor.api.resolver;

import com.example.automation.commerce.domain.Execution;
import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.domain.TaskType;
import com.example.automation.commerce.service.ExecutionService;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.api.ApiExecutionRequest;
import com.example.automation.executor.api.provider.ApiExecutionRequestProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiExecutionRequestResolverTest {

    private ApiExecutionRequestResolver resolver;
    @Mock ExecutionService executionService;
    @Mock ApiExecutionRequestProvider provider;
    @Mock Execution execution;
    private Task task;

    @BeforeEach
    void createResolver() {
        task = Task.create(TaskType.LOW_STOCK_REPORT, "재고", "재고부족처리");
        when(execution.getTask()).thenReturn(task);

    }

    @Test
    void resolver_호출하면_알맞은_provider_가_선택된다() {
        // given
        Long executionId = 1L;
        ExecutionContext executionContext = new ExecutionContext(executionId, ExecutionType.API);
        ApiExecutionRequest expectedRequest =
                new ApiExecutionRequest(
                        "http://localhost:8080/api/notifications",
                        HttpMethod.POST,
                        Map.of("Content-Type", "application/json"),
                        "body"
                );

        when(executionService.findExecution(executionId)).thenReturn(execution);

        when(provider.create(task)).thenReturn(expectedRequest);
        when(provider.getTaskType()).thenReturn(TaskType.LOW_STOCK_REPORT);
        resolver = new ApiExecutionRequestResolver(executionService, List.of(provider));

        // when
        ApiExecutionRequest result = resolver.resolve(executionContext);

        // then
        verify(provider).create(task);

        assertThat(result).isSameAs(expectedRequest);

    }

    @Test
    void resolver_호출했을때_알맞은_provider_가_없으면_예외가_발생한다() {
        // given
        Long executionId = 1L;
        ExecutionContext executionContext = new ExecutionContext(executionId, ExecutionType.API);

        when(executionService.findExecution(executionId)).thenReturn(execution);
        resolver = new ApiExecutionRequestResolver(executionService, List.of());

        // then
        assertThatThrownBy(() -> resolver.resolve(executionContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TaskType.LOW_STOCK_REPORT + "에 대응하는 provider가 존재하지 않습니다.");
    }
}