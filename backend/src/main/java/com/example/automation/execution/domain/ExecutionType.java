package com.example.automation.execution.domain;

public enum ExecutionType {
    AI("AI 사용"),
    API("API 호출"),
    SLACK("SLACK 알림"),
    EMAIL("이메일 알림"),
    RPA("자동화 작업");

    private final String description;

    ExecutionType(String description) {
        this.description = description;
    }
}
