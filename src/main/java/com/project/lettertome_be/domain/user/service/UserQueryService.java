package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    //회원 정보 조회
    public UserResponseDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .writtenLetter(1)//letter 개발 전 임시 값
                .receviedLetter(1)//letter 개발 전 임시 값
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
