package com.example.automation.file.storage;

import com.example.automation.file.config.FileBasePathProperties;
import com.example.automation.file.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final FileBasePathProperties fileBasePathProperties;

    @Override
    public void store(String storageKey, InputStream inputStream, long contentLength) {
        try {
            String basePath = fileBasePathProperties.basePath();
            Path path = Paths.get(basePath);

            Files.createDirectories(path);

            Path resolvePath = path.resolve(storageKey);

            Files.copy(inputStream, resolvePath);
        } catch (IOException e) {
            throw new FileStorageException("파일 저장에 실패했습니다.", e);
        }
    }

    @Override
    public InputStream load(String storageKey) {
        try {
            String basePath = fileBasePathProperties.basePath();

            Path resolvePath = Paths.get(basePath).resolve(storageKey);

            if (!Files.exists(resolvePath)) {
                throw new FileStorageException("파일을 가져올 수 없습니다.");
            }
            return Files.newInputStream(resolvePath);
        } catch (IOException e) {
            throw new FileStorageException("파일을 가져올 수 없습니다.", e);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            String basePath = fileBasePathProperties.basePath();
            Path resolvePath = Paths.get(basePath).resolve(storageKey);

            Files.deleteIfExists(resolvePath);
        } catch (IOException e) {
            throw new FileStorageException("파일을 삭제할 수 없습니다.", e);
        }
    }
}
