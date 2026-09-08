package com.example.automation.notification.dto;

import com.example.automation.notification.domain.Notification;

public record NotificationResponse(
        Long notificationId,
        Long taskId
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getTaskId());
    }
}
