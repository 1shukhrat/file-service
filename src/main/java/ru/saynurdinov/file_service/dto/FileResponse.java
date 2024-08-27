package ru.saynurdinov.file_service.dto;

import java.time.LocalDateTime;

public record FileResponse(String title, String description,
                           LocalDateTime creationDate, String base64Data) {
}
