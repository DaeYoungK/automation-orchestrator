package com.example.automation.task.scheduler;

import com.example.automation.task.config.LowStockProperties;
import com.example.automation.task.application.LowStockTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LowStockTaskScheduler {

    private final LowStockProperties lowStockProperties;
    private final LowStockTaskService lowStockTaskService;

    @Scheduled(cron = "${scheduler.low-stock.cron}")
    public void createLowStockTask() {
        lowStockTaskService.createLowStockReportTasks(
                lowStockProperties.lowStock().threshold()
        );
    }
}
