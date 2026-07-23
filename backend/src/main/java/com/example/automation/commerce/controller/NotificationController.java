package com.example.automation.commerce.controller;

import com.example.automation.commerce.domain.Notification;
import com.example.automation.commerce.dto.NotificationRequest;
import com.example.automation.commerce.dto.NotificationResponse;
import com.example.automation.commerce.service.NotificationService;
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
@RequestMapping("/api/notifications")
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
