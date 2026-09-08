package com.example.automation.notification.controller;

import com.example.automation.notification.domain.Notification;
import com.example.automation.notification.dto.NotificationRequest;
import com.example.automation.notification.dto.NotificationResponse;
import com.example.automation.notification.application.NotificationService;
import com.example.automation.executor.api.url.ApiEndpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.NOTIFICATIONS)
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request
    ) {
        Notification notification = notificationService.createNotification(
                request.taskId(),
                request.title(),
                request.description()
        );

        NotificationResponse response = NotificationResponse.from(notification);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
