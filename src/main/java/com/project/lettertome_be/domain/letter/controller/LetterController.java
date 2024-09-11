package com.project.lettertome_be.domain.letter.controller;

import com.project.lettertome_be.domain.letter.dto.request.CreateLetterRequestDto;
import com.project.lettertome_be.domain.letter.dto.response.CreateLetterResponseDto;
import com.project.lettertome_be.domain.letter.service.LetterService;
import com.project.lettertome_be.global.common.response.ApiResponse;
import com.project.lettertome_be.global.jwt.annotation.CurrentUser;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateLetterResponseDto>> createLetter(
            @RequestBody CreateLetterRequestDto requestDto,
            @CurrentUser AuthUser authUser) {

        CreateLetterResponseDto responseDto = letterService.createLetter(requestDto, authUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(HttpStatus.CREATED, responseDto));
    }
}
