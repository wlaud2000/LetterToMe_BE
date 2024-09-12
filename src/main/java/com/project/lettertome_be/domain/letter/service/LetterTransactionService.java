package com.project.lettertome_be.domain.letter.service;

import com.project.lettertome_be.domain.letter.entity.Letter;
import com.project.lettertome_be.domain.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LetterTransactionService {

    private final LetterRepository letterRepository;

    @Transactional
    public void updateLettersStatus(List<Letter> letters) {
        for (Letter letter : letters) {
            letter.setOpened(true);
        }
        letterRepository.saveAll(letters);  // 여러 편지를 한 번에 저장
    }
}
