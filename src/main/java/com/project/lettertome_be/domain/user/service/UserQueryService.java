package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.UserErrorCode;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    //회원 정보 조회
    public UserResponseDto getUserByEmail(AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        return UserResponseDto.from(user);
    }

}
