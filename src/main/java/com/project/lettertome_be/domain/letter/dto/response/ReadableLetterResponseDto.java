package com.project.lettertome_be.domain.letter.dto.response;

import java.time.LocalDateTime;

public record ReadableLetterResponseDto(

        Long letterId,

        String title,

        LocalDateTime sendAt,

        LocalDateTime updatedAt
) {
}
