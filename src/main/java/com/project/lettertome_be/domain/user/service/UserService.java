package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.UserErrorCode;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화

    // 회원가입
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 이메일 중복 확인
        if(userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .email(signUpRequestDto.email())
                .nickname(signUpRequestDto.nickName())
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .build();

        User savedUser = userRepository.save(user);

        // 저장된 User 엔티티를 DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }

    //비밀번호 변경
    public void changePassword(AuthUser authUser, ChangePasswordRequestDto changePasswordRequestDto) {
        //Controller에서 넘어온 AuthUser에서 email을 추출해서 DB에서 유저 조회를 해서 영속상태로 만듦
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newPassword = changePasswordRequestDto.newPassword();
        user.changePassword(passwordEncoder.encode(newPassword));
        log.info("[User Service] 사용자의 비밀번호가 변경되었습니다.");
    }

    //유저 이메일 변경
    public void changeEmail(AuthUser authUser, ChangeEmailRequestDto changeEmailRequestDto) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newEmail = changeEmailRequestDto.newEmail();
        user.changeEmail(newEmail);
        log.info("[User Service] 이메일이 변경되었습니다 -> {}", newEmail);
    }

    //유저 이름 변경
    public void changeNickName(AuthUser authUser, ChangeNickNameRequestDto changeNickNameRequestDto) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newNickName = changeNickNameRequestDto.newNickName();
        user.changeNickName(newNickName);
        log.info("[User Service] 이름이 변경되었습니다 -> {}", newNickName);
    }

    //유저 삭제
    public void deleteUser(AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        //user 삭제
        userRepository.delete(user);

        log.info("[User Service] 사용자가 성공적으로 삭제되었습니다.");
    }

}
