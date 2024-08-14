package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.entity.LocalUser;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.LocalUserRepository;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final LocalUserRepository localUserRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화

    // 회원가입
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 이메일 중복 확인
        if(userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new RuntimeException("해당 이메일이 이미 존재합니다.");
        }

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .email(signUpRequestDto.email())
                .nickName(signUpRequestDto.nickName())
                .build();

        User savedUser = userRepository.save(user);

        // LocalUser 엔티티 생성 및 저장
        LocalUser localUser = LocalUser.builder()
                .user(savedUser)
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .build();

        localUserRepository.save(localUser);

        // 저장된 User 엔티티를 DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }

    //비밀번호 변경
    public void changePassword(String email, ChangePasswordRequestDto changePasswordRequestDto) {

        String newPassword = changePasswordRequestDto.newPassword();

        //이메일로 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //조회한 user 객체로 LocalUser 조회
        LocalUser localUser = localUserRepository.findByUser(user)
                 .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //조회한 localUser 객체의 비밀번호 변경
        localUser.changePassword(passwordEncoder.encode(newPassword));

        log.info("[User Service] 사용자의 비밀번호가 변경되었습니다.");

        //로그아웃 구현되면 로그아웃 실행
        /*log.info("[User Service] 로그아웃 되었습니다. 다시 로그인 해주세요.");*/
    }

    //유저 이메일 변경
    public void changeEmail(String email, ChangeEmailRequestDto changeEmailRequestDto) {

        String newEmail = changeEmailRequestDto.newEmail();
        //기존 이메일로 user 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //조회한 user의 이메일 변경
        user.changeEmail(newEmail); //조회한 user의 이메일 변경

        log.info("[User Service] 이메일이 변경되었습니다 -> {}", newEmail);


        //로그아웃 구현되면 로그아웃 실행
        /*log.info("[User Service] 로그아웃 되었습니다. 다시 로그인 해주세요.");*/
    }

    //유저 이름 변경
    public void changeNickName(String email, ChangeNickNameRequestDto changeNickNameRequestDto) {

        String newNickName = changeNickNameRequestDto.newNickName();
        //이메일로 user 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        user.changeNickName(newNickName);

        log.info("[User Service] 이름이 변경되었습니다 -> {}", newNickName);
    }

    //유저 삭제
    public void deleteUser(String email) {

        //이메일로 user 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //user로 localUser 조회
        LocalUser localUser = localUserRepository.findByUser(user)
                        .orElseThrow(()-> new NoSuchElementException("LocalUser가 존재하지 않습니다."));

        //user 삭제
        localUserRepository.delete(localUser);
        userRepository.delete(user);

        log.info("[User Service] 사용자가 성공적으로 삭제되었습니다.");
    }
}
