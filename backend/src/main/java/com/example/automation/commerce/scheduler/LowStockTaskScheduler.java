package com.example.automation.commerce.scheduler;

import com.example.automation.commerce.service.LowStockTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LowStockTaskScheduler {

    private long threshold;
    private final LowStockTaskService lowStockTaskService;

    @Scheduled(cron = "0 0 9 * * *")
    public void createLowStockTask() {
        lowStockTaskService.createLowStockReportTasks(threshold);
    }
}
