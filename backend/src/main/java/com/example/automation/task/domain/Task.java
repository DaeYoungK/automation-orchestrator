package com.example.automation.task.domain;

import com.example.automation.common.entity.BaseEntity;
import com.example.automation.execution.domain.Execution;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "task")
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TaskType taskType;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Execution> executions = new ArrayList<>();

    private String title;
    private String description;

    /**
     * 연관관계 편의 메서드
     */
    public void addExecution(Execution execution) {
        this.executions.add(execution);
        execution.assignTask(this);
    }

    protected Task(TaskType taskType, String title, String description) {
        this.taskType = taskType;
        this.title = title;
        this.description = description;
    }

    public static Task create(TaskType taskType, String title, String description) {
        Task task = new Task(taskType, title, description);
        task.taskStatus = TaskStatus.PENDING;

        return task;
    }

    public void approve() {
        validateApprovable();
        this.taskStatus = TaskStatus.APPROVED;
    }

    private void validateApprovable() {
        if (this.taskStatus != TaskStatus.PENDING) {
            throw new IllegalArgumentException("보류중일 경우에만 승인 가능합니다.");
        }
    }

    public void start() {
        validateRunnable();
        this.taskStatus = TaskStatus.RUNNING;
    }

    private void validateRunnable() {
        if (this.taskStatus != TaskStatus.APPROVED) {
            throw new IllegalArgumentException("승인처리 된 경우에만 실행 가능합니다.");
        }
    }

    public void complete() {
        validateRunning();
        this.taskStatus = TaskStatus.SUCCESS;
    }

    public void fail() {
        validateRunning();
        this.taskStatus = TaskStatus.FAILED;
    }

    private void validateRunning() {
        if (this.taskStatus != TaskStatus.RUNNING) {
            throw new IllegalArgumentException("작업 진행중일 경우에만 처리 할 수 있습니다.");
        }
    }

    public void cancel() {
        validateCancelable();
        this.taskStatus = TaskStatus.CANCELED;
    }

    private void validateCancelable() {
        if (this.taskStatus == TaskStatus.FAILED || this.taskStatus == TaskStatus.SUCCESS) {
            throw new IllegalArgumentException("작업을 취소할 수 없습니다.");
        }
    }
}
