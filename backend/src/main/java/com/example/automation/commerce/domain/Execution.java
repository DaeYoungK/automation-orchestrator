package com.example.automation.commerce.domain;

import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "execution")
@NoArgsConstructor
@Getter
public class Execution extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execution_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(value = EnumType.STRING)
    private ExecutionType executionType;

    @Enumerated(value = EnumType.STRING)
    private ExecutionStatus executionStatus;

    private int attemptCount;
    private String errorMessage;

    protected Execution(ExecutionType executionType) {
        this.executionType = executionType;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void assignTask(Task task) {
        this.task = task;
    }

    public static Execution create(ExecutionType executionType) {
        Execution execution = new Execution(executionType);
        execution.executionStatus = ExecutionStatus.READY;

        return execution;
    }

    public void start() {
        validateRunnable();
        this.executionStatus = ExecutionStatus.RUNNING;
    }

    private void validateRunnable() {
        if (this.executionStatus != ExecutionStatus.READY) {
            throw new IllegalArgumentException("준비 중일 경우에만 실행할 수 있습니다.");
        }
    }

    public void complete() {
        validateRunning();
        this.executionStatus = ExecutionStatus.SUCCESS;
    }

    public void fail(String errorMessage) {
        validateRunning();
        this.executionStatus = ExecutionStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    private void validateRunning() {
        if (this.executionStatus != ExecutionStatus.RUNNING) {
            throw new IllegalArgumentException("실행중일 경우에만 가능합니다.");
        }
    }

    public void retry() {
        validateRetryable();
        this.executionStatus = ExecutionStatus.READY;
        this.attemptCount++;
        this.errorMessage = null;
    }

    private void validateRetryable() {
        if (this.executionStatus != ExecutionStatus.FAILED) {
            throw new IllegalArgumentException("실패 상태에서만 재시도할 수 있습니다.");
        }
        if (this.attemptCount >= 3) {
            throw new IllegalArgumentException("재시도 가능 횟수를 초과하였습니다.");
        }
    }

    public void cancel() {
        if (this.executionStatus == ExecutionStatus.SUCCESS || this.executionStatus == ExecutionStatus.FAILED) {
            throw new IllegalArgumentException("작업을 취소할 수 없습니다.");
        }
        this.executionStatus = ExecutionStatus.CANCELED;
    }

}
