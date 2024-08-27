package ru.saynurdinov.file_service.dto;


import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime date) {

    public ErrorResponse(String message) {
        this(message, LocalDateTime.now());
    }
}
