package com.project.lettertome_be.domain.letter.dto.response;

import java.time.LocalDateTime;

public record LetterDetailsResponseDto(

        Long letterId,

        String title,

        String content,

        LocalDateTime sendAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
