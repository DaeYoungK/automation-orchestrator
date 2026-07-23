package com.example.automation.commerce.dto;

import com.example.automation.commerce.domain.Notification;

public record NotificationResponse(
        Long notificationId,
        Long taskId
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getTaskId());
    }
}
