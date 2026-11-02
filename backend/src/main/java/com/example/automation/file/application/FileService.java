package com.example.automation.file.application;

import com.example.automation.file.domain.StoredFile;
import com.example.automation.file.dto.StoreFileRequest;
import com.example.automation.file.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorage fileStorage;
    private final StoredFilePersistenceService storedFilePersistenceService;

    public Long storeFile(StoreFileRequest request) {
        StoredFile storedFile = StoredFile.create(request.originalFilename(), request.sizeBytes(), request.contentType());
        try {
            fileStorage.store(
                    storedFile.getStorageKey(),
                    request.inputStream(),
                    request.sizeBytes()
            );

            return storedFilePersistenceService.createStoredFile(storedFile);

        }catch (RuntimeException e) {
            cleanup(storedFile.getStorageKey(), e);
            throw e;
        }
    }

    private void cleanup(String storageKey, RuntimeException originalException) {
        try {
            fileStorage.delete(storageKey);
        }catch (RuntimeException cleanupException) {
            originalException.addSuppressed(cleanupException);
        }
    }
}
