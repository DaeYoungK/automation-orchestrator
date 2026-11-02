package com.example.automation.file.application;

import com.example.automation.file.domain.StoredFile;
import com.example.automation.file.dto.StoreFileRequest;
import com.example.automation.file.repository.StoredFileRepository;
import com.example.automation.file.storage.FileStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FileServiceIntegrationTest {

    @TempDir static Path tempDir;

    @Autowired FileService fileService;
    @Autowired StoredFileRepository storedFileRepository;
    @Autowired FileStorage fileStorage;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add(
                "file.base-path",
                tempDir::toString
        );
    }

    @AfterEach
    void cleanUp() {
        storedFileRepository.deleteAll();
    }

    @Test
    void File_을_저장하면_실제파일과_메타데이터가_저장된다() throws IOException {
        //given
        String originalFilename = "test.txt";
        byte[] data = originalFilename.getBytes(StandardCharsets.UTF_8);
        long sizeBytes = data.length;
        String contentType = "text/plain";

        InputStream inputStream = new ByteArrayInputStream(data);

        StoreFileRequest request = new StoreFileRequest(originalFilename, sizeBytes, contentType, inputStream);

        //when
        Long storeFileId = fileService.storeFile(request);
        StoredFile storedFile = storedFileRepository.findById(storeFileId).orElseThrow();

        //then
        assertThat(storedFile.getId()).isEqualTo(storeFileId);
        assertThat(storedFile.getOriginalFilename()).isEqualTo(originalFilename);
        assertThat(storedFile.getContentType()).isEqualTo(contentType);
        assertThat(storedFile.getSizeBytes()).isEqualTo(data.length);

        try(InputStream loaded = fileStorage.load(storedFile.getStorageKey())) {
            assertThat(loaded.readAllBytes())
                    .isEqualTo(data);
        }
    }

}