package com.project.lettertome_be.domain.user.controller;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.jwt.userdetails.CustomUserDetails;
import com.project.lettertome_be.domain.user.service.UserQueryService;
import com.project.lettertome_be.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String email = userDetails.getUsername();
        UserResponseDto userResponseDto = userQueryService.getUserByEmail(email);
        return ResponseEntity.ok(userResponseDto);
    }

    //비밀번호 변경(로컬유저만 해당)
    @PutMapping("/pw")
    public ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto) {

        String email = userDetails.getUsername();
        userService.changePassword(email, changePasswordRequestDto);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요"));
    }

    //이메일 변경(로컬유저만 해당)
    @PatchMapping("/email")
    public ResponseEntity<Map<String, String>> changeEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody @Valid ChangeEmailRequestDto changeEmailRequestDto) {

        String email = userDetails.getUsername();
        userService.changeEmail(email, changeEmailRequestDto);
        return ResponseEntity.ok(Map.of("message","이메일이 성공적으로 변경되었습니다. 다시 로그인 해주세요."));
    }
}
