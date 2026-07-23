package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.Notification;
import com.example.automation.commerce.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(Long taskId, String title, String description) {
        Notification notification = Notification.create(taskId, title, description);

        return notificationRepository.save(notification);
    }
}
