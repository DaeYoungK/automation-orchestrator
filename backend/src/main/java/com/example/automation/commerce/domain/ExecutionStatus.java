package com.example.automation.commerce.domain;

public enum ExecutionStatus {
    READY("실행준비"),
    RUNNING("작업실행"),
    RETRYING("작업 재시도"),
    FAILED("작업 실패"),
    SUCCESS("작업 성공"),
    CANCELED("작업 취소");

    private final String description;

    ExecutionStatus (String description) {
        this.description = description;
    }
}
