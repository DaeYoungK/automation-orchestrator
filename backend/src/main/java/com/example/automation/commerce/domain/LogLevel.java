package com.example.automation.commerce.domain;

public enum LogLevel {
    INFO("정보"),
    WARN("경고"),
    ERROR("오류");

    private final String description;

    LogLevel (String description) {
        this.description = description;
    }
}
