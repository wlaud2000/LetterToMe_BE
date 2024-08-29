package com.project.lettertome_be.global.common.sender;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Slf4j
@Component
public class DefaultEmailSender {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final String serviceEmail;

    public DefaultEmailSender(
            final JavaMailSender mailSender,
            final SpringTemplateEngine templateEngine,
            @Value("${spring.mail.username}") final String serviceEmail
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.serviceEmail = serviceEmail;
    }

    public void sendAuthCodeForSignUp(final String targetEmail, final String authCode) {
        // 회원가입 시 이메일로 인증 코드를 전송하는 메서드
        final Context context = new Context(); // 이메일 템플릿에 사용할 컨텍스트를 생성
        context.setVariable("authCode", authCode); // 컨텍스트에 인증 코드를 설정

        final String mailBody = templateEngine.process("EmailAuthCodeTemplate", context); // 이메일 본문을 생성
        sendMail("회원가입 인증번호 메일입니다.", targetEmail, mailBody); // 이메일을 전송
    }

    public void sendAuthCodeForPasswordReset(final String targetEmail, final String authCode) {
        // 회원가입 시 이메일로 인증 코드를 전송하는 메서드
        final Context context = new Context(); // 이메일 템플릿에 사용할 컨텍스트를 생성
        context.setVariable("authCode", authCode); // 컨텍스트에 인증 코드를 설정

        final String mailBody = templateEngine.process("EmailAuthCodeTemplate", context); // 이메일 본문을 생성
        sendMail("비밀번호 변경 인증번호 메일입니다.", targetEmail, mailBody); // 이메일을 전송
    }

    private void sendMail(final String subject, final String email, final String mailBody) {
        // 이메일을 전송하는 메서드
        try {
            final MimeMessage message = mailSender.createMimeMessage(); // MIME 메시지를 생성
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // MIME 메시지 헬퍼를 생성

            helper.setSubject(subject); // 이메일 제목을 설정
            helper.setTo(email); // 수신자를 설정
            helper.setFrom(new InternetAddress(serviceEmail, "Another Art")); // 발신자를 설정
            helper.setText(mailBody, true); // 이메일 본문을 설정

            mailSender.send(message); // 이메일을 전송
        } catch (final Exception e) {
            log.warn("메일 전송 간 오류 발생...", e);
            throw new IllegalArgumentException("메일을 전송할 수 없습니다.");
        }
    }

}