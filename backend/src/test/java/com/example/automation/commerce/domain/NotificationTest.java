package com.example.automation.commerce.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NotificationTest {

    private static final Long taskId = 1L;
    private static final String title = "title";
    private static final String description = "description";

    @Test
    void Notification_생성() {
        //when
        Notification notification = Notification.create(taskId, title, description);

        //then
        assertThat(notification.getTaskId()).isEqualTo(taskId);
        assertThat(notification.getTitle()).isEqualTo(title);
        assertThat(notification.getDescription()).isEqualTo(description);
    }

    @Test
    void Notification_생성시_taskId_가_null_이면_예외가_발생한다() {
        //given
        Long taskId = null;

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("taskId는 필수값입니다.");
    }

    @Test
    void Notification_생성시_title_이_null_이면_예외가_발생한다() {
        //given
        String title = null;

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title은 null일 수 없습니다.");
    }

    @Test
    void Notification_생성시_title_이_비어있으면_예외가_발생한다() {
        //given
        String title = "";

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title은 필수값입니다.");
    }

    @Test
    void Notification_생성시_title_이_공백이면_예외가_발생한다() {
        //given
        String title = "     ";

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title은 필수값입니다.");
    }

    @Test
    void Notification_생성시_description_이_null_이면_예외가_발생한다() {
        //given
        String description = null;

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description은 null일 수 없습니다.");
    }

    @Test
    void Notification_생성시_description_이_비어있으면_예외가_발생한다() {
        //given
        String description = "";

        //when & then
        assertThatThrownBy(() -> Notification.create(taskId, title, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description은 필수값입니다.");
    }
}