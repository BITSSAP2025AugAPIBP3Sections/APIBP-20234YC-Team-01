package com.example.GreenGrub.exception.errorResponse;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorDescription(
        String code,
        String message,
        List<String> suggestions
) {
}
