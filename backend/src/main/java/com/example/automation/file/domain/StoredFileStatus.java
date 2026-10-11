package com.example.automation.file.domain;

public enum StoredFileStatus {
    TEMPORARY("임시저장"),
    ATTACHED("연결완료");

    private final String description;

    StoredFileStatus(String description) {
        this.description = description;
    }
}
