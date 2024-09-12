package com.project.lettertome_be.domain.letter.service;

import com.project.lettertome_be.domain.letter.entity.Letter;
import com.project.lettertome_be.domain.letter.repository.LetterRepository;
import com.project.lettertome_be.global.common.sender.DefaultEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LetterSchedulerService {

    private final LetterRepository letterRepository;
    private final DefaultEmailSender emailSender;
    private final LetterTransactionService letterTransactionService;  // 트랜잭션 서비스 주입

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void sendScheduledLetters() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);

        List<Letter> lettersToSend = letterRepository.findBySendAtBetween(oneMinuteAgo, now);

        if (!lettersToSend.isEmpty()) {
            for (Letter letter : lettersToSend) {
                log.info("Sending letter with ID: {}", letter.getId());
                sendLetterByEmail(letter);  // 비동기로 이메일 발송 처리
            }
            // 한 번에 편지 상태 업데이트
            letterTransactionService.updateLettersStatus(lettersToSend);
        }
    }

    private void sendLetterByEmail(Letter letter) {
        emailSender.sendMailAsync(
                "Your Letter from the Past",  // 제목
                letter.getUser().getEmail(),  // 수신자 이메일
                letter.getContent()  // 이메일 본문
        );
        log.info("Email sent to: {}", letter.getUser().getEmail());
    }
}