package com.example.automation.commerce.domain;

public enum TaskStatus {
    PENDING("보류중"),
    APPROVED("승인됨"),
    RUNNING("실행중"),
    FAILED("실패"),
    SUCCESS("성공"),
    CANCELED("중지");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }
}
