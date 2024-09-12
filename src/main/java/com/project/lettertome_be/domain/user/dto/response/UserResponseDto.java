package com.project.lettertome_be.domain.user.dto.response;

import com.project.lettertome_be.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDto (

        Long userId,

        String email,

        String nickName,

        String profileImg,

        int writtenLetter,

        int receviedLetter,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {

    //엔티티를 DTO로 변환
    public static UserResponseDto from(User user) {

        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .profileImg(user.getProfileImg())
                .writtenLetter(1)
                .receviedLetter(1)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
