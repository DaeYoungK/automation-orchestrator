package com.example.automation.notification.domain;

import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@Getter
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private Long taskId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    private Notification(Long taskId, String title, String description) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
    }

    public static Notification create(Long taskId, String title, String description) {
        validateCreate(taskId, title, description);
        return new Notification(taskId, title, description);
    }

    private static void validateCreate(Long taskId, String title, String description) {
        if (taskId == null) {
            throw new IllegalArgumentException("taskId는 필수값입니다.");
        }
        if (title == null) {
            throw new IllegalArgumentException("title은 null일 수 없습니다.");
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("title은 필수값입니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("description은 null일 수 없습니다.");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("description은 필수값입니다.");
        }
    }
}
