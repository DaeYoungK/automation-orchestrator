package com.example.automation.file.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.InputStream;

public record StoreFileRequest(

        @NotBlank(message = "originalFilename 은 필수값입니다.") @Size(max = 100)
        String originalFilename,

        @NotNull(message = "sizeBytes 은 필수값입니다.")
        Long sizeBytes,

        @NotBlank(message = "contentType 은 필수값입니다.")
        String contentType,

        @NotNull(message = "inputStream 은 필수값입니다.")
        InputStream inputStream
) {
}
