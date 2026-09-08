package com.example.automation.execution.fixture;

import com.example.automation.execution.domain.Execution;
import com.example.automation.execution.domain.ExecutionType;

public class ExecutionFixture {

    public static Execution readyExecution() {
        return Execution.create(ExecutionType.AI);
    }

    public static Execution runningExecution() {
        Execution execution = readyExecution();
        execution.start();
        return execution;
    }

    public static Execution failedExecution() {
        Execution execution = runningExecution();
        execution.fail("작업실패");
        return execution;
    }

    public static Execution retryAndReadyExecution() {
        Execution execution = failedExecution();
        Execution retryExecution = Execution.retryFrom(execution);
        return retryExecution;
    }

    public static Execution successExecution() {
        Execution execution = runningExecution();
        execution.complete();
        return execution;
    }

    public static Execution canceledExecution() {
        Execution execution = runningExecution();
        execution.cancel();
        return execution;
    }
}
