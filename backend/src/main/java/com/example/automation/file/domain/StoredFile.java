package com.example.automation.file.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stored_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stored_file_id")
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String storageKey;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private StoredFileStatus status;

    @Column(nullable = false)
    private Long sizeBytes;
    private String contentType;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected StoredFile(String originalFilename, Long sizeBytes, String contentType) {
        this.originalFilename = originalFilename;
        this.sizeBytes = sizeBytes;
        this.contentType = contentType;
    }

    public static StoredFile create(String originalFilename, Long sizeBytes, String contentType) {
        StoredFile storedFile = new StoredFile(originalFilename, sizeBytes, contentType);
        storedFile.storageKey = UUID.randomUUID().toString();
        storedFile.status = StoredFileStatus.TEMPORARY;

        return storedFile;
    }

    public void attach() {
        validateAttachable();
        this.status = StoredFileStatus.ATTACHED;
    }

    private void validateAttachable() {
        if (this.status != StoredFileStatus.TEMPORARY) {
            throw new IllegalStateException("TEMPORARY 상태의 파일만 ATTACHED로 변경할 수 있습니다.");
        }
    }
}
