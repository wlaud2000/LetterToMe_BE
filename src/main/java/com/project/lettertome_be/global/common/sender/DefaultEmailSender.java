package com.project.lettertome_be.global.common.sender;

import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.ErrorCode;
import com.project.lettertome_be.global.common.response.UserErrorCode;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

    public void sendMail(final String subject, final String email, final String mailBody) {
        try {
            // 발신자와 수신자 이메일 주소 검증
            String sanitizedEmail = email.trim();  // 수신자 이메일의 앞뒤 공백 제거
            if (!isValidEmail(sanitizedEmail)) {
                throw new IllegalArgumentException("유효하지 않은 수신자 이메일 주소입니다: " + sanitizedEmail);
            }

            // 발신자 이메일 검증
            String sanitizedServiceEmail = serviceEmail.trim();  // 발신자 이메일 공백 제거
            if (!isValidEmail(sanitizedServiceEmail)) {
                throw new IllegalArgumentException("유효하지 않은 발신자 이메일 주소입니다: " + sanitizedServiceEmail);
            }

            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject(subject);
            helper.setTo(sanitizedEmail);  // 공백 제거된 수신자 이메일 설정
            helper.setFrom(new InternetAddress(sanitizedServiceEmail, "Letter To Me"));  // 공백 제거된 발신자 이메일 설정
            helper.setText(mailBody, true);

            mailSender.send(message);  // 이메일 전송
        } catch (final MailException e) {
            log.error("이메일 전송 중 오류 발생. 수신자: {}, 제목: {}, 오류: {}", email, subject, e.getMessage());
            throw new CustomException(UserErrorCode.EMAIL_SEND_ERROR); // 사용자 정의 예외
        } catch (final Exception e) {
            log.error("예기치 않은 오류로 메일 전송 실패. 수신자: {}, 제목: {}, 오류: {}", email, subject, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_500);
        }
    }

    // 이메일 주소 유효성 검사 메서드 (정규식 사용)
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";  // 간단한 이메일 정규식
        return email.matches(emailRegex);
    }

}