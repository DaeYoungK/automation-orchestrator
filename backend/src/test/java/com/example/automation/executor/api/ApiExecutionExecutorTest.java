package com.example.automation.executor.api;

import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.executor.ExecutionContext;
import com.example.automation.executor.api.resolver.ApiExecutionRequestResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiExecutionExecutorTest {

    @Mock ApiExecutionRequestResolver resolver;
    @Mock ApiExecutionClient client;
    @InjectMocks ApiExecutionExecutor executor;

    @Test
    void API_Execution을_실행하면_생성된_request를_client에_전달한다() {
        //given
        Long executionId = 1L;
        ExecutionContext executionContext = new ExecutionContext(executionId, ExecutionType.API);
        ApiExecutionRequest expectedRequest =
                new ApiExecutionRequest(
                        "http://localhost:8080/api/notifications",
                        HttpMethod.POST,
                        Map.of("Content-Type", "application/json"),
                        "body"
                );

        when(resolver.resolve(executionContext)).thenReturn(expectedRequest);

        //when
        executor.execute(executionContext);

        //then
        verify(resolver).resolve(executionContext);
        verify(client).execute(expectedRequest);
    }
}