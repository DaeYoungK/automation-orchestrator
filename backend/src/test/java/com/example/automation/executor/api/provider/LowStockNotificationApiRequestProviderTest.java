package com.example.automation.executor.api.provider;

import com.example.automation.executor.api.config.ApiExecutionProperties;
import com.example.automation.task.domain.Task;
import com.example.automation.notification.dto.NotificationRequest;
import com.example.automation.executor.api.ApiExecutionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;


import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LowStockNotificationApiRequestProviderTest {

    @Mock Task task;
    private LowStockNotificationApiRequestProvider provider;

    @BeforeEach
    void setUp() {
        ApiExecutionProperties apiExecutionProperties = new ApiExecutionProperties("http://localhost:8080");
        provider = new LowStockNotificationApiRequestProvider(apiExecutionProperties);
    }


    @Test
    void LOW_STOCK_REPORT_로_notification_API_request_를_생성한다() {
        //given
        when(task.getId()).thenReturn(1L);
        when(task.getTitle()).thenReturn("재고");
        when(task.getDescription()).thenReturn("재고부족처리");

        NotificationRequest requestBody = new NotificationRequest(task.getId(), task.getTitle(), task.getDescription());

        //when
        ApiExecutionRequest request = provider.create(task);

        //then
        assertThat(request.url()).isEqualTo("http://localhost:8080/api/notifications");
        assertThat(request.httpMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.headers()).isEqualTo(Map.of("Content-Type", "application/json"));
        assertThat(request.body()).isEqualTo(requestBody);
    }
}