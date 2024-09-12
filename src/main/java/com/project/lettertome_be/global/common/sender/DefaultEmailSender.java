package com.project.lettertome_be.global.common.sender;

import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.ErrorCode;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
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

    @Async  // 이메일 발송 작업을 비동기로 실행
    public void sendMailAsync(final String subject, final String email, final String mailBody) {
        try {
            sendMail(subject, email, mailBody);  // 실제 이메일 발송은 동기 메서드에서 처리
        } catch (Exception e) {
            // 비동기 예외는 별도로 처리하거나 관리자에게 알림을 보낼 수 있음
            log.error("비동기 메일 발송 중 오류 발생: {}", e.getMessage());
            handleAsyncError(e, email, subject);  // 비동기 작업 실패에 대한 처리
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))  // 재시도 로직 추가
    public void sendMail(final String subject, final String email, final String mailBody) {
        try {
            // 발신자와 수신자 이메일 주소 검증
            String sanitizedEmail = email.trim();
            if (!isValidEmail(sanitizedEmail)) {
                throw new IllegalArgumentException("유효하지 않은 수신자 이메일 주소입니다: " + sanitizedEmail);
            }

            String sanitizedServiceEmail = serviceEmail.trim();
            if (!isValidEmail(sanitizedServiceEmail)) {
                throw new IllegalArgumentException("유효하지 않은 발신자 이메일 주소입니다: " + sanitizedServiceEmail);
            }

            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject(subject);
            helper.setTo(sanitizedEmail);
            helper.setFrom(new InternetAddress(sanitizedServiceEmail, "Letter To Me"));
            helper.setText(mailBody, true);

            mailSender.send(message);  // 이메일 전송
            log.info("이메일 발송 성공: {}", sanitizedEmail);
        } catch (final MailException e) {
            log.error("이메일 전송 중 오류 발생. 수신자: {}, 제목: {}, 오류: {}", email, subject, e.getMessage());
            throw e;  // 재시도 트리거
        } catch (final Exception e) {
            log.error("예기치 않은 오류로 메일 전송 실패. 수신자: {}, 제목: {}, 오류: {}", email, subject, e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Recover  // 재시도 실패 후 처리
    public void recover(MailException e, String email, String subject) {
        log.error("이메일 발송 재시도 후 실패. 수신자: {}, 제목: {}", email, subject);
        // 관리자 알림 또는 실패 처리 로직 추가 가능
    }

    private void handleAsyncError(Exception e, String email, String subject) {
        log.error("비동기 작업 오류 처리: 수신자 {}, 제목 {}, 오류: {}", email, subject, e.getMessage());
        // 관리자에게 알림, 이메일 발송 실패 로그 기록 등의 로직 추가 가능
    }

    // 이메일 주소 유효성 검사 메서드 (정규식 사용)
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";  // 간단한 이메일 정규식
        return email.matches(emailRegex);
    }

}