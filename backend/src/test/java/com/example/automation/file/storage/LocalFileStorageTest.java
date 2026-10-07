package com.example.automation.file.storage;

import com.example.automation.file.config.FileBasePathProperties;
import com.example.automation.file.exception.FileStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LocalFileStorageTest {

    @TempDir
    Path tempDir;

    private LocalFileStorage localFileStorage;

    @BeforeEach
    void setUp() {
        FileBasePathProperties fileBasePathProperties = new FileBasePathProperties(tempDir.toString());
        localFileStorage = new LocalFileStorage(fileBasePathProperties);
    }

    @Test
    void 파일을_저장하면_storageKey_경로에_저장된다() throws IOException {
        //given
        String storageKey = "test-key";
        byte[] data = "inputStream".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Path resultPath = tempDir.resolve(storageKey);

        //when
        localFileStorage.store(storageKey, inputStream, data.length);

        //then
        assertThat(Files.exists(resultPath)).isTrue();
        assertThat(Files.readString(resultPath)).isEqualTo("inputStream");
    }

    @Test
    void 같은_storageKey_로_두번_저장하면_예외가_발생한다() {
        //given
        String storageKey = "test-key";

        byte[] firstData = "first".getBytes();
        byte[] secondData = "second".getBytes();
        InputStream first = new ByteArrayInputStream(firstData);
        InputStream second = new ByteArrayInputStream(secondData);

        //when
        localFileStorage.store(storageKey, first, firstData.length);

        //then
        assertThatThrownBy(() -> localFileStorage.store(storageKey, second, secondData.length))
                .isInstanceOf(FileStorageException.class);
    }

    @Test
    void 저장된_파일을_load_하면_원본_내용이_나온다() throws IOException {
        //given
        String storageKey = "test-key";
        byte[] data = "inputStream".getBytes();

        try (InputStream inputStream = new ByteArrayInputStream(data)){
            localFileStorage.store(storageKey, inputStream, data.length);
        }

        //when && then
        try(InputStream loadFile = localFileStorage.load(storageKey)) {
            assertThat(loadFile.readAllBytes()).isEqualTo(data);
        }
    }

    @Test
    void 존재하지_않는_storageKey_를_load_하면_FileStorageException_이_발생한다(){
        //given
        String notExistStorageKey = "no-exist-key";

        //when && then
        assertThatThrownBy(() -> localFileStorage.load(notExistStorageKey))
                .isInstanceOf(FileStorageException.class);
    }

    @Test
    void 저장된_파일을_delete_하면_실제_파일이_삭제된다() throws IOException {
        //given
        String storageKey = "test-key";
        byte[] data = "inputStream".getBytes();

        try (InputStream inputStream = new ByteArrayInputStream(data)){
            localFileStorage.store(storageKey, inputStream, data.length);
        }

        //when
        assertThat(Files.exists(tempDir.resolve(storageKey))).isTrue();
        localFileStorage.delete(storageKey);

        //then
        assertThat(Files.exists(tempDir.resolve(storageKey))).isFalse();
    }

    @Test
    void 존재하지_않는_파일을_delete_해도_예외가_발생하지_않는다() {
        //given
        String storageKey = "test-key";

        //when && then
        assertDoesNotThrow(() -> localFileStorage.delete(storageKey));
    }
}