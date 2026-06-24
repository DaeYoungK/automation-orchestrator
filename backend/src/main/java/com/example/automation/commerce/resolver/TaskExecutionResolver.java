package com.example.automation.commerce.resolver;

import com.example.automation.commerce.domain.ExecutionType;
import com.example.automation.commerce.domain.TaskType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskExecutionResolver {

    public List<ExecutionType> resolve(TaskType taskType) {
        return switch (taskType) {
            case PURCHASE_REQUEST_REVIEW -> List.of(ExecutionType.EMAIL);
            case LOW_STOCK_REPORT -> List.of(ExecutionType.AI);
            case PAYMENT_FAILURE_REPORT -> List.of(ExecutionType.API);
            case VOC_ANALYSIS_REPORT -> List.of(ExecutionType.AI);
            case RPA_FAILURE_RECOVERY -> List.of(ExecutionType.RPA);
        };
    }
}
