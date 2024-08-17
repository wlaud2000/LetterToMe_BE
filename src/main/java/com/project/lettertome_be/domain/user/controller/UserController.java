package com.project.lettertome_be.domain.user.controller;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.jwt.annotation.AuthUser;
import com.project.lettertome_be.domain.user.service.UserQueryService;
import com.project.lettertome_be.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

        SignUpResponseDto responseDto = userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //회원 정보 조회
    @GetMapping("")
    public ResponseEntity<UserResponseDto> getUser(@AuthUser User user) {
        UserResponseDto userResponseDto = userQueryService.getUserByEmail(user);
        return ResponseEntity.ok(userResponseDto);
    }

    //비밀번호 변경(로컬유저만 해당)
    @PutMapping("/pw")
    public ResponseEntity<Map<String, String>> changePassword(@AuthUser User user,
                                                              @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        userService.changePassword(user, requestDto);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요"));
    }

    //이메일 변경(로컬유저만 해당)
    @PatchMapping("/email")
    public ResponseEntity<Map<String, String>> changeEmail(@AuthUser User user,
                                                           @RequestBody @Valid ChangeEmailRequestDto requestDto) {
        userService.changeEmail(user, requestDto);
        return ResponseEntity.ok(Map.of("message","이메일이 성공적으로 변경되었습니다. 다시 로그인 해주세요."));
    }

    //이름 변경
    @PatchMapping("/nickname")
    public ResponseEntity<Map<String, String>> changeNickName(@AuthUser User user,
                                                              @RequestBody @Valid ChangeNickNameRequestDto requestDto) {
        userService.changeNickName(user, requestDto);
        return ResponseEntity.ok(Map.of("message", "이름이 성공적으로 변경되었습니다."));
    }

    //회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<Map<String, String>> deleteUser(@AuthUser User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
    }

    //테스트
    @GetMapping("/user")
    public User user(@AuthUser User user) {
        return user;
    }

}
