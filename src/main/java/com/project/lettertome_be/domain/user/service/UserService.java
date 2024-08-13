package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.entity.LocalUser;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.LocalUserRepository;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final LocalUserRepository localUserRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화

    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        //이메일 중복 확인
        if(userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new RuntimeException("해당 이메일이 이미 존재합니다.");
        }

        //User 엔티티 생성 및 저장
        User user = User.builder()
                .email(signUpRequestDto.email())
                .nickName(signUpRequestDto.nickName())
                .build();

        User savedUser = userRepository.save(user);

        //LocalUser 엔티티 생성 및 저장
        LocalUser localUser = LocalUser.builder()
                .user(savedUser)
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .build();

        localUserRepository.save(localUser);

        // 저장된 User 엔티티를 DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }
}
