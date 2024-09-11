package com.project.lettertome_be.domain.letter.service;

import com.project.lettertome_be.domain.letter.entity.Letter;
import com.project.lettertome_be.domain.letter.repository.LetterRepository;
import com.project.lettertome_be.global.common.sender.DefaultEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LetterSchedulerService {

    private final LetterRepository letterRepository;
    private final DefaultEmailSender emailSender;

    @Transactional
    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void sendScheduledLetters() {

        LocalDateTime now = LocalDateTime.now();  // 현재 시간
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);  // 1분 전까지의 시간

        // 1분 내에 발송해야 할 편지 조회
        List<Letter> lettersToSend = letterRepository.findBySendAtBetween(oneMinuteAgo, now);

        if (!lettersToSend.isEmpty()) {
            for (Letter letter : lettersToSend) {
                log.info("Sending letter with ID: {}", letter.getId());
                sendLetterByEmail(letter); // 편지를 이메일로 발송
                letter.setOpened(true); // 편지의 상태를 '발송됨'으로 변경
            }
            // 한번에 업데이트
            letterRepository.saveAll(lettersToSend);
        }
    }

    private void sendLetterByEmail(Letter letter) {
        // 편지 내용을 이메일로 전송
        emailSender.sendMail(
                "Your Letter from the Past",  // 제목
                letter.getUser().getEmail(),  // 수신자 이메일
                letter.getContent()  // 이메일 본문
        );
        log.info("Email sent to: {}", letter.getUser().getEmail());
    }
}

