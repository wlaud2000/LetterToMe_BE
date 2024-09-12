package com.project.lettertome_be.domain.letter.dto.response;

import com.project.lettertome_be.domain.letter.entity.Letter;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
public record CreateLetterResponseDto(

        Long letterId,

        String title,

        String content,

        LocalDateTime sendAt,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Integer dDay

) {

    // Letter 엔티티에서 CreateLetterResponseDto로 변환하는 from() 메서드
    public static CreateLetterResponseDto from(Letter letter) {
        return CreateLetterResponseDto.builder()
                .letterId(letter.getId())
                .title(letter.getTitle())
                .content(letter.getContent())
                .sendAt(letter.getSendAt())
                .createdAt(letter.getCreatedAt())
                .updatedAt(letter.getUpdatedAt())
                .dDay(calculateDDay(letter.getSendAt()))  // dDay 계산
                .build();
    }

    // dDay 계산 메서드
    private static int calculateDDay(LocalDateTime sendAt) {
        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(today, sendAt.toLocalDate());
    }
}
