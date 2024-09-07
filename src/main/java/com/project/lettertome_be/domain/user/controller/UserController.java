package com.project.lettertome_be.domain.user.controller;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.service.UserQueryService;
import com.project.lettertome_be.domain.user.service.UserService;
import com.project.lettertome_be.global.common.response.ApiResponse;
import com.project.lettertome_be.global.jwt.annotation.CurrentUser;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

        SignUpResponseDto responseDto = userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(HttpStatus.CREATED, responseDto));
    }

    //회원 정보 조회
    @GetMapping("")
    public ApiResponse<UserResponseDto> getUser(@CurrentUser AuthUser authUser) {
        UserResponseDto userResponseDto = userQueryService.getUserByEmail(authUser);
        return ApiResponse.onSuccess(userResponseDto);
    }

    //비밀번호 변경(로컬유저만 해당)
    @PutMapping("/pw")
    public ApiResponse<String> changePassword(@CurrentUser AuthUser authUser,
                                              @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        userService.changePassword(authUser, requestDto);
        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요");
    }

    //이메일 변경(로컬유저만 해당)
    @PatchMapping("/email")
    public ApiResponse<String> changeEmail(@CurrentUser AuthUser authUser,
                                           @RequestBody @Valid ChangeEmailRequestDto requestDto) {
        userService.changeEmail(authUser, requestDto);
        return ApiResponse.onSuccess("이메일이 성공적으로 변경되었습니다. 다시 로그인 해주세요.");
    }

    //이름 변경
    @PatchMapping("/nickname")
    public ApiResponse<String> changeNickName(@CurrentUser AuthUser authUser,
                                              @RequestBody @Valid ChangeNickNameRequestDto requestDto) {
        userService.changeNickName(authUser, requestDto);
        return ApiResponse.onSuccess("이름이 성공적으로 변경되었습니다.");
    }

    //회원 탈퇴
    @DeleteMapping("")
    public ApiResponse<String> deleteUser(@CurrentUser AuthUser authUser) {
        userService.deleteUser(authUser);
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    //테스트
    @GetMapping("/user")
    public AuthUser user(@CurrentUser AuthUser authUser) {
        return authUser;
    }

}
