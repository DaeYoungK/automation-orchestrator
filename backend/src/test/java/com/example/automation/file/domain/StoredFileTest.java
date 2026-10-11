package com.example.automation.file.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StoredFileTest {

    private StoredFile storedFile;

    @BeforeEach
    void createStoredFile() {
        String originalFileName = "테스트파일.xlsx";
        Long sizeBytes = 100L;
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        storedFile = StoredFile.create(originalFileName, sizeBytes, contentType);
    }

    @Test
    void StoredFile_생성시_TEMPORARY_상태고_storageKey_가_자동_생성된다() {
        //then
        assertThat(storedFile.getStatus()).isEqualTo(StoredFileStatus.TEMPORARY);
        assertThat(storedFile.getStorageKey()).isNotBlank();

    }

    @Test
    void TEMPORARY_상태인_파일을_attach_하면_ATTACHED_상태가_된다() {
        //when
        storedFile.attach();

        //then
        assertThat(storedFile.getStatus()).isEqualTo(StoredFileStatus.ATTACHED);
    }

    @Test
    void ATTACHED_상태인_파일을_attach_하면_예외가_발생한다() {
        //when
        storedFile.attach();

        //when & then
        assertThatThrownBy(() -> storedFile.attach())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("TEMPORARY 상태의 파일만 ATTACHED로 변경할 수 있습니다.");
    }
}