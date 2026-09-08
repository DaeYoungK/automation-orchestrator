package com.example.automation.task.domain;

public enum TaskType {
    PURCHASE_REQUEST_REVIEW("발주 검토 요청"),
    LOW_STOCK_REPORT("재고 부족 리포트"),
    PAYMENT_FAILURE_REPORT("결제 실패 리포트"),
    VOC_ANALYSIS_REPORT("상품 불만 분석 리포트"),
    RPA_FAILURE_RECOVERY("RPA 실패 복구 요청")
    ;

    private final String description;

    TaskType(String description) {
        this.description = description;
    }
}
