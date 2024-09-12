package com.project.lettertome_be.domain.letter.dto.response;

import java.time.LocalDateTime;

public record UnreadableLetterResponseDto(

        Long letterId,

        String title,

        LocalDateTime sendAt,

        LocalDateTime updatedAt,

        Integer dDay
) {
}
