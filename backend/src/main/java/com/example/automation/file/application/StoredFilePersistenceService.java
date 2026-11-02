package com.example.automation.file.application;

import com.example.automation.file.domain.StoredFile;
import com.example.automation.file.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoredFilePersistenceService {

    private final StoredFileRepository storedFileRepository;

    @Transactional
    public Long createStoredFile(StoredFile storedFile) {
        return storedFileRepository.save(storedFile).getId();
    }
}
