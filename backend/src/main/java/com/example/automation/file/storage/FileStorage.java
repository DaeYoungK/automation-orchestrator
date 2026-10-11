package com.example.automation.file.storage;

import java.io.InputStream;

public interface FileStorage {

    void store(String storageKey, InputStream inputStream, long contentLength);

    InputStream load(String storageKey);

    void delete(String storageKey);
}
