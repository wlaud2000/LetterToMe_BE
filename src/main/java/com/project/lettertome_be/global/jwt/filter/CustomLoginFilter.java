package com.project.lettertome_be.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.lettertome_be.domain.user.dto.request.LoginRequestDto;
import com.project.lettertome_be.global.common.response.ApiResponse;
import com.project.lettertome_be.global.jwt.dto.JwtDto;
import com.project.lettertome_be.global.jwt.exception.SecurityErrorCode;
import com.project.lettertome_be.global.jwt.userdetails.CustomUserDetails;
import com.project.lettertome_be.global.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(@NonNull HttpServletRequest request,
                                                @NonNull HttpServletResponse response) throws AuthenticationException {

        log.info("[ Login Filter ] 로그인 시도: Custom Login Filter 작동 ");
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequestDto requestBody;

        try {
            // Request Body를 읽어 DTO로 변환
            requestBody = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error("[ Login Filter ] Request Body 파싱 중 IOException 발생: {}", e.getMessage());
            throw new AuthenticationServiceException("Request Body 파싱 중 오류가 발생하였습니다.");
        }

        // Request Body에서 이메일과 비밀번호 추출
        String email = requestBody.email();
        String password = requestBody.password();

        log.info("[ Login Filter ] Email ---> {} ", email);
        log.info("[ Login Filter ] Password ---> {} ", password);

        // UsernamePasswordAuthenticationToken 생성 (인증용 객체)
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password, null);

        log.info("[ Login Filter ] 인증용 객체 UsernamePasswordAuthenticationToken 이 생성되었습니다. ");
        log.info("[ Login Filter ] 인증을 시도합니다.");

        // 인증 시도
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            @NonNull FilterChain chain,
                                            @NonNull Authentication authentication) throws IOException {

        log.info("[ Login Filter ] 로그인에 성공하였습니다.");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        JwtDto jwtDto = JwtDto.builder()
                .accessToken(jwtUtil.createJwtAccessToken(customUserDetails))
                .refreshToken(jwtUtil.createJwtRefreshToken(customUserDetails))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // JSON 형식의 응답 본문 작성
        response.getWriter().write(objectMapper.writeValueAsString(jwtDto));
    }

    @Override
    protected void unsuccessfulAuthentication(@NonNull HttpServletRequest request,
                                              @NonNull HttpServletResponse response,
                                              @NonNull AuthenticationException failed) throws IOException {

        log.info("[ Login Filter ] 로그인에 실패하였습니다.");

        SecurityErrorCode errorCode = getErrorCode(failed);

        log.error("[ Login Filter ] 인증 실패: {}", errorCode.getMessage());

        // 예외 발생 시 처리
        handleError(response, errorCode);
    }

    private void handleError(HttpServletResponse response, SecurityErrorCode errorCode) {
        try {
            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(
                    ApiResponse.onFailure(errorCode.getCode(), errorCode.getMessage())
            ));
        } catch (IOException e) {
            log.error("[ Login Filter ] 응답 작성 중 IOException 발생: {}", e.getMessage());
        }
    }

    private SecurityErrorCode getErrorCode(AuthenticationException failed) {
        if (failed instanceof BadCredentialsException) {
            log.error("[ Login Filter ] BadCredentialsException 발생: 잘못된 인증 정보입니다.");
            return SecurityErrorCode.BAD_CREDENTIALS;
        } else if (failed instanceof LockedException || failed instanceof DisabledException) {
            log.error("[ Login Filter ] 계정이 잠기거나 비활성화된 상태입니다.");
            return SecurityErrorCode.FORBIDDEN;
        } else if (failed instanceof UsernameNotFoundException) {
            log.error("[ Login Filter ] UsernameNotFoundException 발생: 계정을 찾을 수 없습니다.");
            return SecurityErrorCode.USER_NOT_FOUND;
        } else if (failed instanceof AuthenticationServiceException) {
            log.error("[ Login Filter ] AuthenticationServiceException 발생: 서버 내부 오류.");
            return SecurityErrorCode.INTERNAL_SECURITY_ERROR;
        } else {
            log.error("[ Login Filter ] 예상치 못한 예외 발생: {}", failed.getClass().getSimpleName());
            return SecurityErrorCode.UNAUTHORIZED;
        }
    }

}