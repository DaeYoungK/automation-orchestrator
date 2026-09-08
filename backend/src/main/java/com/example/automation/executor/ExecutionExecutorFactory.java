package com.example.automation.executor;

import com.example.automation.execution.domain.ExecutionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExecutionExecutorFactory {

    private final Map<ExecutionType, ExecutionExecutor> executors;

    public ExecutionExecutorFactory(List<ExecutionExecutor> executors) {
        this.executors = executors.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ExecutionExecutor::getExecutionType,
                        Function.identity()
                ));
    }

    public ExecutionExecutor getExecutor(ExecutionType executionType) {
        Objects.requireNonNull(executionType, "ExecutionType은 null일 수 없습니다.");
        ExecutionExecutor executor = executors.get(executionType);

        if (executor == null) {
            throw new IllegalArgumentException(
                    executionType.name() + " 타입의 Executor가 존재하지 않습니다."
            );
        }
        return executor;
    }
}
