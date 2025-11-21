package com.example.GreenGrub.exception.errorResponse;

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
        LocalDateTime timeStamp
) {
}
