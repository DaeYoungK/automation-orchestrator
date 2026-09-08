package com.example.automation.execution.domain;

import com.example.automation.task.domain.Task;
import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "execution")
@NoArgsConstructor
@Getter
public class Execution extends BaseEntity {

    private static final int MAX_RETRY_COUNT = 3;

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

    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionLog> executionLogs = new ArrayList<>();

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

    public void addLog(ExecutionLog executionLog) {
        this.executionLogs.add(executionLog);
        executionLog.assignExecution(this);
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

    public static Execution retryFrom(Execution failedExecution) {
        validateRetryable(failedExecution);
        Execution execution = create(failedExecution.executionType);
        execution.attemptCount = failedExecution.attemptCount + 1;

        return execution;
    }

    private static void validateRetryable(Execution failedExecution) {
        if (!failedExecution.isFailed()) {
            throw new IllegalArgumentException("실패 상태에서만 재시도할 수 있습니다.");
        }
        if (!failedExecution.hasRetryAttemptsRemaining()) {
            throw new IllegalArgumentException("재시도 가능 횟수를 초과하였습니다.");
        }
    }

    private boolean isFailed() {
        return executionStatus == ExecutionStatus.FAILED;
    }

    private boolean hasRetryAttemptsRemaining() {
        return attemptCount < MAX_RETRY_COUNT;
    }

    public boolean isRetryable() {
        return isFailed() && hasRetryAttemptsRemaining();
    }


    public void cancel() {
        if (this.executionStatus == ExecutionStatus.SUCCESS || this.executionStatus == ExecutionStatus.FAILED) {
            throw new IllegalArgumentException("작업을 취소할 수 없습니다.");
        }
        this.executionStatus = ExecutionStatus.CANCELED;
    }

}
