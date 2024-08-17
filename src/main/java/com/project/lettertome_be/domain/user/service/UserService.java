package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
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
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .build();

        User savedUser = userRepository.save(user);

        // 저장된 User 엔티티를 DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }

    //비밀번호 변경
    public void changePassword(User user, ChangePasswordRequestDto changePasswordRequestDto) {

        String newPassword = changePasswordRequestDto.newPassword();

        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //조회한 localUser 객체의 비밀번호 변경
        dbUser.changePassword(passwordEncoder.encode(newPassword));

        log.info("[User Service] 사용자의 비밀번호가 변경되었습니다.");

        //로그아웃 구현되면 로그아웃 실행
        /*log.info("[User Service] 로그아웃 되었습니다. 다시 로그인 해주세요.");*/
    }

    //유저 이메일 변경
    public void changeEmail(User user, ChangeEmailRequestDto changeEmailRequestDto) {

        String newEmail = changeEmailRequestDto.newEmail();
        //기존 이메일로 user 조회
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //조회한 user의 이메일 변경
        dbUser.changeEmail(newEmail); //조회한 user의 이메일 변경

        log.info("[User Service] 이메일이 변경되었습니다 -> {}", newEmail);


        //로그아웃 구현되면 로그아웃 실행
        /*log.info("[User Service] 로그아웃 되었습니다. 다시 로그인 해주세요.");*/
    }

    //유저 이름 변경
    public void changeNickName(User user, ChangeNickNameRequestDto changeNickNameRequestDto) {

        String newNickName = changeNickNameRequestDto.newNickName();
        //이메일로 user 조회
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        dbUser.changeNickName(newNickName);

        log.info("[User Service] 이름이 변경되었습니다 -> {}", newNickName);
    }

    //유저 삭제
    public void deleteUser(User user) {

        //이메일로 user 조회
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        //user 삭제
        userRepository.delete(dbUser);

        log.info("[User Service] 사용자가 성공적으로 삭제되었습니다.");
    }

}
