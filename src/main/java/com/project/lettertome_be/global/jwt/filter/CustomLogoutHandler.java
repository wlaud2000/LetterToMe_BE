package com.project.lettertome_be.global.jwt.filter;

import com.project.lettertome_be.global.common.response.ApiResponse;
import com.project.lettertome_be.global.common.util.HttpResponseUtil;
import com.project.lettertome_be.global.jwt.exception.SecurityErrorCode;
import com.project.lettertome_be.global.jwt.service.TokenService;
import com.project.lettertome_be.global.jwt.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            String accessToken = jwtUtil.resolveAccessToken(request);

            if (accessToken == null) {
                log.warn("[ CustomLogoutHandler ] Access Token 이 없습니다.");
                HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        ApiResponse.onFailure(HttpStatus.UNAUTHORIZED,"SEC401_0", "Access Token 이 없습니다."));
                return;
            }

            // 토큰 유효성 검증: 예외 발생 시 catch 블록으로 이동
            jwtUtil.validateToken(accessToken);

            // 액세스 토큰 남은 유효시간 계산
            long remainingTime = jwtUtil.getRemainingExpiration(accessToken);

            // 만료 시간이 0보다 큰 경우에만 블랙리스트에 추가
            if (remainingTime > 0) {
                tokenService.addToBlacklist(accessToken, remainingTime);
                log.info("[ CustomLogoutHandler ] Access Token 블랙리스트 처리 완료.");
            }

            String email = jwtUtil.getEmail(accessToken);
            String refreshToken = tokenService.getRefreshTokenByEmail(email);

            if (refreshToken != null) {
                tokenService.deleteTokenByEmail(email);
                log.info("[ CustomLogoutHandler ] 리프레시 토큰 삭제 완료.");
            } else {
                log.info("[ CustomLogoutHandler ] 리프레시 토큰이 없으므로 Access Token 만 블랙리스트 처리.");
            }

            HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK,"로그아웃이 완료되었습니다.");

        } catch (ExpiredJwtException e) {
            log.warn("[ CustomLogoutHandler ] Access Token 이 만료되었습니다.");
            try {
                HttpResponseUtil.setErrorResponse(response, SecurityErrorCode.TOKEN_EXPIRED.getHttpStatus(),
                        SecurityErrorCode.TOKEN_EXPIRED.getErrorResponse());
            } catch (IOException ioException) {
                log.error("[ CustomLogoutHandler ] 응답 처리 중 IOException 발생: {}", ioException.getMessage());
            }
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("[ CustomLogoutHandler ] 유효하지 않은 토큰입니다.");
            try {
                HttpResponseUtil.setErrorResponse(response, SecurityErrorCode.INVALID_TOKEN.getHttpStatus(),
                        SecurityErrorCode.INVALID_TOKEN.getErrorResponse());
            } catch (IOException ioException) {
                log.error("[ CustomLogoutHandler ] 응답 처리 중 IOException 발생: {}", ioException.getMessage());
            }
        } catch (Exception e) {
            log.error("[ CustomLogoutHandler ] 로그아웃 처리 중 오류 발생: {}", e.getMessage());
            try {
                HttpResponseUtil.setErrorResponse(response, SecurityErrorCode.INTERNAL_SECURITY_ERROR.getHttpStatus(),
                        SecurityErrorCode.INTERNAL_SECURITY_ERROR.getErrorResponse());
            } catch (IOException ioException) {
                log.error("[ CustomLogoutHandler ] 응답 처리 중 IOException 발생: {}", ioException.getMessage());
            }
        }
    }
}
