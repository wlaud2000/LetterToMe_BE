package com.project.lettertome_be.domain.user.jwt.dto;

import lombok.Builder;

@Builder
public record JwtDto (

        String accessToken,

        String refreshToken
){
}
