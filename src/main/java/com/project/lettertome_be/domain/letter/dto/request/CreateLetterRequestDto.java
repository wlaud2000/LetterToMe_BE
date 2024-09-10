package com.project.lettertome_be.domain.letter.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateLetterRequestDto(

        String title,

        String content,

        LocalDateTime sendAt
) {
}
