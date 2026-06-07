package com.example.automation.commerce.domain;

import com.example.automation.commerce.fixture.TaskFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    static Stream<Arguments> notApprovedTask() {
        return Stream.of(
                Arguments.of(TaskStatus.APPROVED.name(), TaskFixture.approvedTask()),
                Arguments.of(TaskStatus.RUNNING.name(), TaskFixture.runningTask()),
                Arguments.of(TaskStatus.SUCCESS.name(), TaskFixture.successTask()),
                Arguments.of(TaskStatus.FAILED.name(), TaskFixture.failedTask()),
                Arguments.of(TaskStatus.CANCELED.name(), TaskFixture.canceledTask())
        );
    }

    static Stream<Arguments> notRunningTask() {
        return Stream.of(
                Arguments.of(TaskStatus.PENDING.name(), TaskFixture.pendingTask()),
                Arguments.of(TaskStatus.RUNNING.name(), TaskFixture.runningTask()),
                Arguments.of(TaskStatus.SUCCESS.name(), TaskFixture.successTask()),
                Arguments.of(TaskStatus.FAILED.name(), TaskFixture.failedTask()),
                Arguments.of(TaskStatus.CANCELED.name(), TaskFixture.canceledTask())
        );
    }

    static Stream<Arguments> notSuccessAndFailedTask() {
        return Stream.of(
                Arguments.of(TaskStatus.PENDING.name(), TaskFixture.pendingTask()),
                Arguments.of(TaskStatus.APPROVED.name(), TaskFixture.approvedTask()),
                Arguments.of(TaskStatus.SUCCESS.name(), TaskFixture.successTask()),
                Arguments.of(TaskStatus.FAILED.name(), TaskFixture.failedTask()),
                Arguments.of(TaskStatus.CANCELED.name(), TaskFixture.canceledTask())
        );
    }

    static Stream<Arguments> notCanceledTask() {
        return Stream.of(
                Arguments.of(TaskStatus.SUCCESS.name(), TaskFixture.successTask()),
                Arguments.of(TaskStatus.FAILED.name(), TaskFixture.failedTask())
        );
    }

    @Test
    void Task_를_생성하면_기본_상태가_PENDING_이다() {
        //given & when
        Task task = TaskFixture.pendingTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    void Task_를_승인하면_상태가_APPROVED_이다() {
        //given & when
        Task task = TaskFixture.approvedTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.APPROVED);
    }

    @ParameterizedTest(name = "{0} 상태에서는 승인 불가")
    @MethodSource("notApprovedTask")
    void Task_의_상태가_PENDING_이_아닌상태에서_승인하면_예외발생(String state, Task task) {
        //when & then
        assertThatThrownBy(task::approve)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보류중일 경우에만 승인 가능합니다.");
    }

    @Test
    void Task_를_Start_하면_상태가_RUNNING_이다() {
        //given & when
        Task task = TaskFixture.runningTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @ParameterizedTest(name = "{0} 상태에서는 시작 불가")
    @MethodSource("notRunningTask")
    void Task_의_상태가_APPROVED_가_아닌상태에서_시작하면_예외발생(String state, Task task) {
        //when & then
        assertThatThrownBy(task::start)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("승인처리 된 경우에만 실행 가능합니다.");
    }

    @Test
    void Task_를_complete_하면_상태가_SUCCESS_이다() {
        //given & when
        Task task = TaskFixture.successTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.SUCCESS);
    }

    @ParameterizedTest(name = "{0} 상태에서는 성공 불가")
    @MethodSource("notSuccessAndFailedTask")
    void Task_의_상태가_RUNNING_이_아닌상태에서_성공하면_예외발생(String state, Task task) {
        //when & then
        assertThatThrownBy(task::complete)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작업 진행중일 경우에만 처리 할 수 있습니다.");
    }

    @Test
    void Task_를_fail_하면_상태가_FAILED_이다() {
        //given & when
        Task task = TaskFixture.failedTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.FAILED);
    }

    @ParameterizedTest(name = "{0} 상태에서는 실패 불가")
    @MethodSource("notSuccessAndFailedTask")
    void Task_의_상태가_RUNNING_이_아닌상태에서_실패하면_예외발생(String state, Task task) {
        //when & then
        assertThatThrownBy(task::fail)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작업 진행중일 경우에만 처리 할 수 있습니다.");
    }

    @Test
    void Task_를_cancel_하면_상태가_CANCELED_이다() {
        //given & when
        Task task = TaskFixture.canceledTask();

        //then
        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.CANCELED);
    }

    @ParameterizedTest(name = "{0} 상태에서는 취소 불가")
    @MethodSource("notCanceledTask")
    void Task_의_상태가_SUCCESS_거나_FAILED_일때_취소하면_예외발생(String state, Task task) {
        //when & then
        assertThatThrownBy(task::cancel)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작업을 취소할 수 없습니다.");
    }
}