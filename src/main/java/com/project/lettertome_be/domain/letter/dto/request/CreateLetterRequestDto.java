package com.project.lettertome_be.domain.letter.dto.request;

import com.project.lettertome_be.domain.letter.entity.Letter;
import com.project.lettertome_be.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateLetterRequestDto(

        String title,

        String content,

        LocalDateTime sendAt
) {

        public Letter toEntity(User user) {
            return Letter.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .sendAt(sendAt)
                    .build();
        }
}
