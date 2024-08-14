package com.project.lettertome_be.domain.user.controller;

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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

        SignUpResponseDto responseDto = userService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String email = userDetails.getUsername();
        UserResponseDto userResponseDto = userQueryService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }
}
