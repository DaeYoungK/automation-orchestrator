package com.example.automation.commerce.domain;

import com.example.automation.commerce.fixture.ExecutionFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ExecutionTest {

    static Stream<Arguments> notRunningExecution() {
        return Stream.of(
                Arguments.of(ExecutionStatus.RUNNING.name(), ExecutionFixture.runningExecution()),
                Arguments.of(ExecutionStatus.FAILED.name(), ExecutionFixture.failedExecution()),
                Arguments.of(ExecutionStatus.SUCCESS.name(), ExecutionFixture.successExecution()),
                Arguments.of(ExecutionStatus.CANCELED.name(), ExecutionFixture.canceledExecution())
        );
    }

    static Stream<Arguments> notRunningExecutions() {
        return Stream.of(
                Arguments.of(ExecutionStatus.READY.name(), ExecutionFixture.readyExecution()),
                Arguments.of(ExecutionStatus.READY.name(), ExecutionFixture.retryAndReadyExecution()),
                Arguments.of(ExecutionStatus.SUCCESS.name(), ExecutionFixture.successExecution()),
                Arguments.of(ExecutionStatus.FAILED.name(), ExecutionFixture.failedExecution()),
                Arguments.of(ExecutionStatus.CANCELED.name(), ExecutionFixture.canceledExecution())
        );
    }

    static Stream<Arguments> notCanceledExecution() {
        return Stream.of(
                Arguments.of(ExecutionStatus.SUCCESS.name(), ExecutionFixture.successExecution()),
                Arguments.of(ExecutionStatus.FAILED.name(), ExecutionFixture.failedExecution())
        );
    }

    static Stream<Arguments> notRetryExecution() {
        return Stream.of(
                Arguments.of(ExecutionStatus.READY.name(), ExecutionFixture.readyExecution()),
                Arguments.of(ExecutionStatus.RUNNING.name(), ExecutionFixture.runningExecution()),
                Arguments.of(ExecutionStatus.RETRYING.name(), ExecutionFixture.retryAndReadyExecution()),
                Arguments.of(ExecutionStatus.SUCCESS.name(), ExecutionFixture.successExecution()),
                Arguments.of(ExecutionStatus.CANCELED.name(), ExecutionFixture.canceledExecution())
        );
    }

    @Test
    void Execution_을_생성하면_기본_상태는_READY_이고_재시도_횟수는_0이다() {
        //given & when
        Execution execution = ExecutionFixture.readyExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.READY);
        assertThat(execution.getAttemptCount()).isEqualTo(0);
    }

    @Test
    void Execution_을_실행하면_상태는_RUNNING_이다() {
        //given & when
        Execution execution = ExecutionFixture.runningExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.RUNNING);
    }

    @Test
    void Execution_이_성공하면_상태는_SUCCESS_이다() {
        //given & when
        Execution execution = ExecutionFixture.successExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.SUCCESS);
    }

    @Test
    void Execution_이_실패하면_상태는_Failed_이다() {
        //given & when
        Execution execution = ExecutionFixture.failedExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.FAILED);
    }

    @Test
    void Execution_을_재시도하면_상태는_READY_이고_재시도횟수가_증가한다() {
        //given & when
        Execution execution = ExecutionFixture.retryAndReadyExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.READY);
        assertThat(execution.getAttemptCount()).isEqualTo(1);
    }

    @Test
    void Execution_을_취소하면_상태는_CANCELED_이다() {
        //given & when
        Execution execution = ExecutionFixture.canceledExecution();

        //then
        assertThat(execution.getExecutionStatus()).isEqualTo(ExecutionStatus.CANCELED);
    }
    
    @Test
    void Execution_의_재시도횟수가_3번을_초과하면_예외발생() {
        //given
        Execution execution = ExecutionFixture.readyExecution();
        int maxRetry = 3;

        //when
        for (int i = 0; i < maxRetry; i++) {
            execution.start();
            execution.fail("오류발생");
            execution.retry();
        }

        //then
        assertThat(execution.getAttemptCount()).isEqualTo(3);
        assertThatThrownBy(() -> {
            execution.start();
            execution.fail("오류발생");
            execution.retry();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재시도 가능 횟수를 초과하였습니다.");
    }

    @ParameterizedTest(name = "{0} 상태에서는 실행 불가")
    @MethodSource(value = "notRunningExecution")
    void Execution_의_상태가_READY_가_아닐경우_실행하면_예외발생(String state, Execution execution) {
        assertThatThrownBy(execution::start)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("준비 중일 경우에만 실행할 수 있습니다.");
    }

    @ParameterizedTest(name = "{0} 상태에서는 성공 불가")
    @MethodSource(value = "notRunningExecutions")
    void Execution_의_상태가_RUNNING_이_아닐경우_성공하면_예외발생(String state, Execution execution) {
        assertThatThrownBy(execution::complete)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("실행중일 경우에만 가능합니다.");
    }

    @ParameterizedTest(name = "{0} 상태에서는 실패 불가")
    @MethodSource(value = "notRunningExecutions")
    void Execution_의_상태가_RUNNING_이_아닐경우_실패하면_예외발생(String state, Execution execution) {
        assertThatThrownBy(() -> execution.fail("작업실패"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("실행중일 경우에만 가능합니다.");
    }

    @ParameterizedTest(name = "{0} 상태에서는 재시도 불가")
    @MethodSource("notRetryExecution")
    void Execution_의_상태가_FAILED_가_아닐경우_재시도하면_예외발생(String state, Execution execution) {
        assertThatThrownBy(execution::retry)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("실패 상태에서만 재시도할 수 있습니다.");
    }

    @ParameterizedTest(name = "{0} 상태에서는 취소 불가")
    @MethodSource(value = "notCanceledExecution")
    void Execution_의_상태가_SUCCESS_거나_FAILED_일경우_취소하면_예외발생(String state, Execution execution) {
        assertThatThrownBy(execution::cancel)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작업을 취소할 수 없습니다.");
    }
}