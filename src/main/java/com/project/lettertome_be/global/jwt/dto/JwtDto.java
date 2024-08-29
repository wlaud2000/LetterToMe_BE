package com.project.lettertome_be.global.jwt.dto;

import lombok.Builder;

@Builder
public record JwtDto (

        String accessToken,

        String refreshToken
){

}
