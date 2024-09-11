package com.project.lettertome_be.domain.letter.service;

import com.project.lettertome_be.domain.letter.dto.request.CreateLetterRequestDto;
import com.project.lettertome_be.domain.letter.dto.response.CreateLetterResponseDto;
import com.project.lettertome_be.domain.letter.entity.Letter;
import com.project.lettertome_be.domain.letter.repository.LetterRepository;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.UserErrorCode;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    public CreateLetterResponseDto createLetter(CreateLetterRequestDto requestDto, AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        // dDay를 계산해서 편지 엔티티에 추가
        Letter letter = requestDto.toEntity(user);
        letter.setDDay(calculateDDay(letter.getSendAt()));  // dDay 계산 추가

        Letter savedLetter = letterRepository.save(letter);

        return CreateLetterResponseDto.from(savedLetter);
    }

    private int calculateDDay(LocalDateTime sendAt) {
        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(today, sendAt.toLocalDate());
    }

}
