package com.project.lettertome_be.domain.auth.controller;

import com.project.lettertome_be.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        Map<String, String> response = authService.reissueAccessToken(refreshToken);
        log.info("[ Auth Controller ] 토큰을 재발급합니다. ");
        return ResponseEntity.ok(response);
    }

}



