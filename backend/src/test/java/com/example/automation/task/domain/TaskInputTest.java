package com.example.automation.task.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskInputTest {


    @Test
    void TaskInput_을_생성하면_TaskInputType_이_FILE_이다() {
        //given
        String key = "TestKey";
        Long storedFileId = 1L;

        //when
        TaskInput taskInput = TaskInput.createFile(key, storedFileId);

        //then
        assertThat(taskInput.getTaskInputType()).isEqualTo(TaskInputType.FILE);
        assertThat(taskInput.getKey()).isEqualTo(key);
        assertThat(taskInput.getStoredFileId()).isEqualTo(storedFileId);
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    void TaskInput_생성시_key_가_비어있으면_예외가_발생한다(String key) {
        //given
        Long storedFileId = 1L;

        //when && then
        assertThatThrownBy(() -> TaskInput.createFile(key, storedFileId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TaskInput key는 필수입니다.");
    }

    @Test
    void TaskInput_생성시_storedFileId_값이_null_이면_예외가_발생한다() {
        //given
        String key = "TestKey";
        Long storedFileId = null;

        //when && then
        assertThatThrownBy(() -> TaskInput.createFile(key, storedFileId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("FILE 타입은 storedFileId가 필수입니다.");
    }

}