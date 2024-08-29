package com.project.lettertome_be.global.jwt.filter;

import com.project.lettertome_be.global.jwt.service.TokenService;
import com.project.lettertome_be.global.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //request에서 Access Token 추출
        String accessToken = jwtUtil.resolveAccessToken(request);

        if(accessToken != null) {
            //Token에서 이메일 추출
            String email = jwtUtil.getEmail(accessToken);

            //이메일로 refreshToken 조회
            String refreshToken = tokenService.getRefreshTokenByEmail(email);

            //조회 한 refreshToken을 blacklist에 추가
            tokenService.addToBlacklist(refreshToken);

            //이메일로 저장된 리프레시 토큰 삭제
            tokenService.deleteTokenByEmail(email);
        }
    }

}
