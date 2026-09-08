package com.example.automation.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotNull(message = "taskId는 필수값입니다.")
        Long taskId,

        @NotBlank(message = "title은 필수값입니다.")
        @Size(max = 100)
        String title,

        @NotBlank(message = "description은 필수값입니다.")
        @Size(max = 255)
        String description
) {
}
