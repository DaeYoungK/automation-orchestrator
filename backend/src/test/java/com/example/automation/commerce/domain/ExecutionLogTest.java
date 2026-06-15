package com.example.automation.commerce.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ExecutionLogTest {

    @Test
    void INFO_WARN_ERROR_로그를_생성할_수_있다() {
        //given & when
        ExecutionLog infoLog = ExecutionLog.create("로그테스트1", LogLevel.INFO);
        ExecutionLog warnLog = ExecutionLog.create("로그테스트2", LogLevel.WARN);
        ExecutionLog errorLog = ExecutionLog.create("로그테스트3", LogLevel.ERROR);

        //then
        assertThat(infoLog.getLogLevel()).isEqualTo(LogLevel.INFO);
        assertThat(warnLog.getLogLevel()).isEqualTo(LogLevel.WARN);
        assertThat(errorLog.getLogLevel()).isEqualTo(LogLevel.ERROR);
    }

    @Test
    void Execution_에_Log_를_추가하면_Execution_이_연결된다() {
        //given
        Execution execution = Execution.create(ExecutionType.AI);
        ExecutionLog log = ExecutionLog.create("로그테스트", LogLevel.INFO);

        //when
        execution.addLog(log);

        //then
        assertThat(execution.getExecutionLogs())
                .hasSize(1)
                .extracting("message", "logLevel")
                .containsExactly(
                        tuple("로그테스트", LogLevel.INFO)
                );
        assertThat(execution.getExecutionLogs())
                .extracting(ExecutionLog::getExecution)
                .containsOnly(execution);
        assertThat(log.getExecution()).isEqualTo(execution);
    }

}