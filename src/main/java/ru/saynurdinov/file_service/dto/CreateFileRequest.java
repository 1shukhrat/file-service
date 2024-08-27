package ru.saynurdinov.file_service.dto;

import jakarta.validation.constraints.NotBlank;
import ru.saynurdinov.file_service.annotation.Base64;

import java.time.LocalDateTime;

public record CreateFileRequest(@NotBlank String title,
                                String description,
                                LocalDateTime creationDate,
                                @NotBlank @Base64 String base64Data) {
}
