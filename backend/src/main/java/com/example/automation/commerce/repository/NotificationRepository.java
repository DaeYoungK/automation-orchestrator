package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
