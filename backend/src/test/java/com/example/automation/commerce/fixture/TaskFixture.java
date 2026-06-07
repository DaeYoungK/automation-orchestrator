package com.example.automation.commerce.fixture;

import com.example.automation.commerce.domain.Task;
import com.example.automation.commerce.domain.TaskType;

public class TaskFixture {

    public static Task pendingTask() {
        return Task.create(TaskType.LOW_STOCK_REPORT, "재고 부족 리포트", "재고 부족 상품 분석");
    }

    public static Task approvedTask() {
        Task task = pendingTask();
        task.approve();
        return task;
    }

    public static Task runningTask() {
        Task task = approvedTask();
        task.start();
        return task;
    }

    public static Task failedTask() {
        Task task = runningTask();
        task.fail();
        return task;
    }

    public static Task successTask() {
        Task task = runningTask();
        task.complete();
        return task;
    }

    public static Task canceledTask() {
        Task task = approvedTask();
        task.cancel();
        return task;
    }
}
