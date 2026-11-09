package com.example.automation.task.domain;

import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "task_input",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_task_input_task_id_input_key",
                        columnNames = {"task_id", "input_key"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskInput extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_input_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "input_key", nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_input_type", nullable = false)
    private TaskInputType taskInputType;

    @Column(name = "stored_file_id", nullable = false)
    private Long storedFileId;

    /**
     * 연관관계 편의 메서드
     */
    protected void assignTask(Task task) {
        this.task = task;
    }

    private TaskInput(String key, TaskInputType taskInputType, Long storedFileId) {
        validateKey(key);

        this.key = key;
        this.taskInputType = taskInputType;
        this.storedFileId = storedFileId;
    }


    public static TaskInput createFile(String key, Long storedFileId) {
        validateStoredFileId(storedFileId);

        return new TaskInput(key, TaskInputType.FILE, storedFileId);
    }

    private static void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("TaskInput key는 필수입니다.");
        }
    }

    private static void validateStoredFileId(Long storedFileId) {
        if (storedFileId == null) {
            throw new IllegalArgumentException("FILE 타입은 storedFileId가 필수입니다.");
        }
    }

}
