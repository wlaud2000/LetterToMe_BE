package com.project.lettertome_be.domain.auth.controller;

import com.project.lettertome_be.domain.auth.service.EmailAuthService;
import com.project.lettertome_be.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;
    private final JwtUtil jwtUtil;

    // 회원가입 시 이메일 인증 코드를 전송하는 엔드포인트
    @PostMapping("/send-signup")
    public ResponseEntity<?> sendSignUpEmailAuthCode(@RequestParam("email") String email) {
        emailAuthService.sendSignUpEmailAuthCode(email); // 회원가입용 이메일 인증 코드 전송
        return ResponseEntity.ok().body(Map.of("message", "이메일로 인증 코드를 전송했습니다."));
    }

    // 회원가입 시 이메일 인증 코드를 검증하는 엔드포인트
    @PostMapping("/verify-signup")
    public ResponseEntity<?> verifySignUpEmailAuthCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        emailAuthService.verifyEmailAuthCode(email, code); // 이메일 인증 코드 검증 (회원가입용)
        return ResponseEntity.ok().body(Map.of("message", "성공적으로 이메일 인증이 완료되었습니다."));
    }

    // 비밀번호 재설정 시 이메일 인증 코드를 전송하는 엔드포인트
    @PostMapping("/send-reset")
    public ResponseEntity<?> sendPasswordResetEmailAuthCode(@RequestParam("email") String email) {
        emailAuthService.sendPasswordResetEmailAuthCode(email); // 비밀번호 재설정용 이메일 인증 코드 전송
        return ResponseEntity.ok().body(Map.of("message", "이메일로 인증 코드를 전송했습니다."));
    }

    // 비밀번호 재설정 시 이메일 인증 코드를 검증하고 임시 AccessToken을 발급하는 엔드포인트
    @PostMapping("/verify-reset")
    public ResponseEntity<?> verifyResetEmailAuthCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        emailAuthService.verifyEmailAuthCode(email, code); // 이메일 인증 코드 검증 (비밀번호 재설정용)

        //임시 AccessToken 생성
        String temporaryToken = jwtUtil.createTemporaryToken(email, 5 * 60 * 1000); // 5분 유효

        return ResponseEntity.ok().body(Map.of("message", "성공적으로 이메일 인증이 완료되었습니다.","temporaryToken", temporaryToken));
    }

}