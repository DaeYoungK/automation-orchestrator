package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.TaskType;
import com.example.automation.commerce.dto.CreateTaskRequest;
import com.example.automation.commerce.dto.LowStockProductResponse;
import com.example.automation.commerce.repository.InventoryQueryMapper;
import com.example.automation.commerce.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LowStockTaskService {

    private final InventoryQueryMapper inventoryQueryMapper;
    private final TaskService  taskService;

    @Transactional
    public List<Long> createLowStockReportTasks(long threshold) {
        List<LowStockProductResponse> lowStockProducts = inventoryQueryMapper.findLowStockProducts(threshold);
        List<Long> taskIds = new ArrayList<>();

        for (LowStockProductResponse product : lowStockProducts) {
            String title = "[재고부족] " + product.getProductName();
            String description =
                    "상품 [" + product.getProductName() + "] 의 남은 재고가 "
                    + product.getAvailableQuantity()
                    + "개 입니다. 현재 상품 상태는 "
                    + product.getProductStatus()
                    + "입니다.";

            CreateTaskRequest createTaskRequest = new CreateTaskRequest(TaskType.LOW_STOCK_REPORT, title, description);

            Long taskId = taskService.createTask(createTaskRequest);
            taskIds.add(taskId);
        }

        return taskIds;
    }
}
