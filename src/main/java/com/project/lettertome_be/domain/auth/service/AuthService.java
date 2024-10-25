package com.project.lettertome_be.domain.auth.service;

import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.jwt.dto.JwtDto;
import com.project.lettertome_be.global.jwt.exception.SecurityErrorCode;
import com.project.lettertome_be.global.jwt.service.TokenService;
import com.project.lettertome_be.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public JwtDto reissueAccessToken(String refreshToken) {

        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");

        try {
            // Refresh Token에서 사용자 이메일 추출
            String email = jwtUtil.getEmail(refreshToken);
            log.info("[ Auth Service ] Email ---> {}", email);

            // Refresh Token 유효성 검사
            jwtUtil.validateToken(refreshToken);

            // 이메일로 저장된 Refresh Token 가져오기
            String storedRefreshToken = tokenService.getRefreshTokenByEmail(email);
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                log.info("[ Auth Service ] 저장된 리프레시 토큰과 일치하지 않습니다.");
                throw new CustomException(SecurityErrorCode.REFRESH_TOKEN_NOT_FOUND);
            }

            log.info("[ Auth Service ] Refresh Token이 유효합니다. 새로운 토큰을 발급합니다.");
            return jwtUtil.reissueToken(refreshToken);

        } catch (SecurityException e) {
            log.error("[ Auth Service ] 보안 예외 발생: {}", e.getMessage());
            throw new SecurityException("유효하지 않은 리프레시 토큰입니다.", e);
        }
    }
}
