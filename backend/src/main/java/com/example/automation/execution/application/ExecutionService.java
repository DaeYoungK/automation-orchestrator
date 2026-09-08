package com.example.automation.execution.application;


import com.example.automation.execution.repository.ExecutionRepository;
import com.example.automation.task.repository.TaskRepository;
import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.domain.ExecutionLog;
import com.example.automation.execution.domain.ExecutionType;
import com.example.automation.execution.domain.LogLevel;
import com.example.automation.task.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final TaskRepository taskRepository;

    public Execution findExecution(Long executionId) {
        return executionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("Execution 이 존재하지 않습니다."));
    }

    public Long createExecution(Long taskId, ExecutionType executionType) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task 가 존재하지 않습니다."));

        Execution execution = Execution.create(executionType);

        task.addExecution(execution);

        execution.addLog(ExecutionLog.create(
                executionType.name() + " 작업을 생성합니다.",
                LogLevel.INFO));

        return executionRepository.save(execution).getId();
    }

    public void startExecution(Long executionId) {
        Execution execution = findExecution(executionId);

        execution.start();

        execution.addLog(ExecutionLog.create(
                execution.getExecutionType().name() + " 작업을 실행합니다.",
                LogLevel.INFO));
    }

    public void successExecution(Long executionId) {
        Execution execution = findExecution(executionId);

        execution.complete();

        execution.addLog(ExecutionLog.create(
                execution.getExecutionType().name() + " 작업이 성공하였습니다.",
                LogLevel.INFO));
    }

    public void failExecution(Long executionId, String failMessage) {
        Execution execution = findExecution(executionId);

        execution.fail(failMessage);

        execution.addLog(ExecutionLog.create(
                execution.getExecutionType().name() + " 작업이 실패하였습니다.",
                LogLevel.ERROR));
    }

    public Long createRetryExecution(Long failedExecutionId) {
        Execution failedExecution = findExecution(failedExecutionId);

        if (failedExecution.getTask() == null) {
            throw new IllegalStateException("Task에 연결된 Execution만 재시도할 수 있습니다.");
        }

        Execution retryExecution = Execution.retryFrom(failedExecution);

        Task task = failedExecution.getTask();
        task.addExecution(retryExecution);


        failedExecution.addLog(ExecutionLog.create(
                failedExecution.getExecutionType().name() + " 재시도 작업을 생성합니다.",
                LogLevel.INFO));
        retryExecution.addLog(ExecutionLog.create(
                retryExecution.getExecutionType().name() + " 재시도 작업이 생성되었습니다.",
                LogLevel.INFO));

        return executionRepository.save(retryExecution).getId();
    }

    public void cancelExecution(Long executionId) {
        Execution execution = findExecution(executionId);

        execution.cancel();

        execution.addLog(ExecutionLog.create(
                execution.getExecutionType().name() + " 작업을 취소합니다.",
                LogLevel.INFO));
    }
}
