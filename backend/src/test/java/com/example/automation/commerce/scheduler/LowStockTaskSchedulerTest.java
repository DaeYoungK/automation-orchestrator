package com.example.automation.commerce.scheduler;

import com.example.automation.commerce.config.LowStockProperties;
import com.example.automation.commerce.config.SchedulerProperties;
import com.example.automation.commerce.service.LowStockTaskService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LowStockTaskSchedulerTest {

    @Test
    void createLowStockTask_를_호출하면_lowStockTaskService_가_호출된다() {
        //given
        LowStockTaskService lowStockTaskService = mock(LowStockTaskService.class);

        SchedulerProperties schedulerProperties = new SchedulerProperties(
                new SchedulerProperties.LowStock("0 0 9 * * *")
        );

        LowStockProperties lowStockProperties = new LowStockProperties(
                new LowStockProperties.LowStock(5L)
        );

        LowStockTaskScheduler scheduler =
                new LowStockTaskScheduler(schedulerProperties, lowStockProperties, lowStockTaskService);

        //when
        scheduler.createLowStockTask();

        //then
        verify(lowStockTaskService).createLowStockReportTasks(5L);
    }

}