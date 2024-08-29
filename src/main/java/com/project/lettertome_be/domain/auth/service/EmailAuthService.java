package com.project.lettertome_be.domain.auth.service;

import com.project.lettertome_be.domain.user.repository.UserRepository;
import com.project.lettertome_be.global.common.sender.DefaultEmailSender;
import com.project.lettertome_be.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {
    private final RedisUtil redisUtil;
    private final DefaultEmailSender emailSender;
    private final UserRepository userRepository;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis; //인증 코드의 유효시간

    // 회원가입 시 이메일 인증 코드를 생성하고 전송하는 메서드
    public void sendSignUpEmailAuthCode(String email) {
        // 이메일이 이미 DB에 존재하는지 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String authCode = createCode(); // 인증 코드 생성
        emailSender.sendAuthCodeForSignUp(email, authCode); // 이메일로 인증 코드 전송
        redisUtil.save(email + ":code", authCode, authCodeExpirationMillis, TimeUnit.MILLISECONDS); // Redis에 인증 코드 저장
    }

    // 이메일 인증 코드를 검증하는 메서드
    public void verifyEmailAuthCode(String email, String authCode) {
        String storedAuthCode = (String) redisUtil.get(email);

        if (!authCode.equals(storedAuthCode)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        redisUtil.delete(email); // 인증 성공 시 Redis에서 인증 코드 삭제
    }

    // 비밀번호 재설정 시 이메일 인증 코드를 생성하고 전송하는 메서드
    public void sendPasswordResetEmailAuthCode(String email) {
        // 이메일이 DB에 존재하는지 확인
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        String authCode = createCode(); // 인증 코드 생성
        emailSender.sendAuthCodeForPasswordReset(email, authCode); // 비밀번호 재설정을 위한 인증 코드 전송
        redisUtil.save(email + ":code", authCode, authCodeExpirationMillis, TimeUnit.MILLISECONDS); // Redis에 인증 코드 저장
    }

    // 인증 코드 생성 메서드
    private String createCode() {
        int length = 8; // 인증 코드 길이
        StringBuilder builder = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789"; // 소문자와 숫자 포함

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length()); // 무작위 인덱스 생성
            builder.append(characters.charAt(randomIndex)); // 해당 인덱스의 문자를 추가
        }

        return builder.toString(); // 생성된 인증 코드 반환
    }
}
