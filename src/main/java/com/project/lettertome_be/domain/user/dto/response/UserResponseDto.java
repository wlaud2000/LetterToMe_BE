package com.project.lettertome_be.domain.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDto (

        Long userId,

        String email,

        String nickName,

        int writtenLetter,

        int receviedLetter,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
