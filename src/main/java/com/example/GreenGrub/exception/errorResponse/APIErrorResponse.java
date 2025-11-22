package com.example.GreenGrub.exception.errorResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record APIErrorResponse(
        String code,
        String message,
        List<String> suggestions,
        String path,
        String correlationId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeStamp
) {
}
