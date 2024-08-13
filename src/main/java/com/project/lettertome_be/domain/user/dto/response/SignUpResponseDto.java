package com.project.lettertome_be.domain.user.dto.response;

import com.project.lettertome_be.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SignUpResponseDto(

        Long userId,

        String email,

        String nickName,

        LocalDateTime createdAt
) {

    //Entity를 DTO로 변환 (정적 팩토리 메서드)
    public static SignUpResponseDto from(User user) {

        return SignUpResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
