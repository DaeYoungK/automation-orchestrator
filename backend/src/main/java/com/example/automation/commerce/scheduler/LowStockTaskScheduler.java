package com.example.automation.commerce.scheduler;

import com.example.automation.commerce.config.LowStockProperties;
import com.example.automation.commerce.config.SchedulerProperties;
import com.example.automation.commerce.service.LowStockTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LowStockTaskScheduler {

    private final SchedulerProperties schedulerProperties;
    private final LowStockProperties lowStockProperties;
    private final LowStockTaskService lowStockTaskService;

    @Scheduled(cron = "#{@schedulerProperties.lowStock().cron()}")
    public void createLowStockTask() {
        lowStockTaskService.createLowStockReportTasks(
                lowStockProperties.lowStock().threshold()
        );
    }
}
