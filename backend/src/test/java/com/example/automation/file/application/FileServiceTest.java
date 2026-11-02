package com.example.automation.file.application;

import com.example.automation.file.domain.StoredFile;
import com.example.automation.file.dto.StoreFileRequest;
import com.example.automation.file.storage.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock StoredFilePersistenceService storedFilePersistenceService;
    @Mock FileStorage fileStorage;

    @InjectMocks FileService fileService;

    StoreFileRequest request;

    @BeforeEach
    void init() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);

        request = new StoreFileRequest(
                "test.txt",
                (long) data.length,
                "text/plain",
                new ByteArrayInputStream(data)
        );
    }

    @Test
    void 메타데이터_저장에_실패하면_저장한_실제파일을_cleanup_한다() {
        //given
        String failMessage = "파일 메타데이터 저장 실패";
        RuntimeException exception = new RuntimeException(failMessage);

        ArgumentCaptor<String> storageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> deleteKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StoredFile> storedFileCaptor = ArgumentCaptor.forClass(StoredFile.class);

        Mockito.doThrow(exception)
                .when(storedFilePersistenceService)
                .createStoredFile(Mockito.any(StoredFile.class));

        //when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> fileService.storeFile(request));

        //then
        assertThat(thrown).isSameAs(exception);
        Mockito.verify(fileStorage).store(
                storageKeyCaptor.capture(),
                Mockito.eq(request.inputStream()) ,
                Mockito.eq(request.sizeBytes())
        );
        Mockito.verify(storedFilePersistenceService).createStoredFile(storedFileCaptor.capture());
        Mockito.verify(fileStorage).delete(deleteKeyCaptor.capture());
        assertThat(storedFileCaptor.getValue().getStorageKey()).isEqualTo(storageKeyCaptor.getValue());
        assertThat(storageKeyCaptor.getValue()).isEqualTo(deleteKeyCaptor.getValue());
    }

    @Test
    void 메타데이터_저장과_cleanup_모두_실패하면_원래예외를_유지하고_cleanup예외를_suppressed로_남긴다() {
        //given
        String failMessage = "파일 메타데이터 저장 실패";
        String deleteFailMessage = "파일 삭제 실패";
        RuntimeException exception = new RuntimeException(failMessage);
        RuntimeException deleteException = new RuntimeException(deleteFailMessage);

        ArgumentCaptor<String> storageKeyCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.doThrow(exception)
                .when(storedFilePersistenceService)
                .createStoredFile(Mockito.any(StoredFile.class));

        Mockito.doThrow(deleteException)
                .when(fileStorage)
                .delete(Mockito.anyString());

        //when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> fileService.storeFile(request)
        );

        //then
        assertThat(thrown).isSameAs(exception).hasMessage(failMessage);
        Mockito.verify(fileStorage).store(
                storageKeyCaptor.capture(),
                Mockito.eq(request.inputStream()) ,
                Mockito.eq(request.sizeBytes())
        );
        Mockito.verify(fileStorage).delete(storageKeyCaptor.getValue());

        assertThat(thrown.getSuppressed())
                .containsExactly(deleteException);
    }
    
    @Test
    void 실제파일_저장에_실패하면_동일한_storageKey로_cleanup하고_메타데이터는_저장하지_않는다() {
        //given
        String failMessage = "파일 저장 실패";
        RuntimeException exception = new RuntimeException(failMessage);

        ArgumentCaptor<String> storageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> deleteKeyCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.doThrow(exception)
                .when(fileStorage)
                .store(Mockito.anyString(), Mockito.any(InputStream.class), Mockito.anyLong());

        //when
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> fileService.storeFile(request)
        );

        //then
        assertThat(thrown).isSameAs(exception);
        Mockito.verify(fileStorage).store(
                storageKeyCaptor.capture(),
                Mockito.eq(request.inputStream()) ,
                Mockito.eq(request.sizeBytes())
        );
        Mockito.verify(fileStorage).delete(deleteKeyCaptor.capture());
        assertThat(deleteKeyCaptor.getValue()).isEqualTo(storageKeyCaptor.getValue());
        Mockito.verifyNoInteractions(storedFilePersistenceService);
    }
}
