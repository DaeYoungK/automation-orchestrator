package com.example.automation.task.application;

import com.example.automation.commerce.inventory.domain.Inventory;
import com.example.automation.commerce.product.domain.Product;
import com.example.automation.commerce.inventory.repository.InventoryRepository;
import com.example.automation.commerce.product.repository.ProductRepository;
import com.example.automation.task.repository.TaskRepository;
import com.example.automation.task.domain.Task;
import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.domain.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class LowStockTaskServiceTest {

    @Autowired
    LowStockTaskService lowStockTaskService;
    @Autowired ProductRepository productRepository;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired TaskRepository taskRepository;

    private void saveProductWithInventory(Product product, Inventory inventory) {
        productRepository.save(product);
        inventoryRepository.save(inventory);
    }

    @Test
    void 재고부족_상품수만큼_LOW_STOCK_REPORT_Task_가_생성된다() {
        //given
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);

        Inventory inventory1 = Inventory.create(product1, 10);
        Inventory inventory2 = Inventory.create(product2, 20);

        saveProductWithInventory(product1, inventory1);
        saveProductWithInventory(product2, inventory2);

        inventory1.reserve(6);
        inventory1.decrease(6);
        inventory2.reserve(16);
        inventory2.decrease(16);

        productRepository.flush();
        inventoryRepository.flush();

        long threshold = 5;

        //when
        List<Long> lowStockReportTaskIds = lowStockTaskService.createLowStockReportTasks(threshold);
        Task task1 = taskRepository.findById(lowStockReportTaskIds.get(0)).orElseThrow();
        Task task2 = taskRepository.findById(lowStockReportTaskIds.get(1)).orElseThrow();

        //then
        assertThat(lowStockReportTaskIds).hasSize(2);
        assertThat(task1.getTaskType()).isEqualTo(TaskType.LOW_STOCK_REPORT);
        assertThat(task2.getTaskType()).isEqualTo(TaskType.LOW_STOCK_REPORT);
        assertThat(task1.getTaskStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task2.getTaskStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(List.of(task1.getDescription(), task2.getDescription()))
                .anyMatch(description -> description.contains("상품1") && description.contains("4"))
                .anyMatch(description -> description.contains("상품2") && description.contains("4"));
    }
}