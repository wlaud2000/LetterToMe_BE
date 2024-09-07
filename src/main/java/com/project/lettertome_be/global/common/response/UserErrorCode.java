package com.project.lettertome_be.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    // 사용자 관련 에러
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_0", "해당 이메일이 이미 존재합니다."),
    USER_NOT_FOUND_404(HttpStatus.NOT_FOUND, "USER404_0", "사용자가 존재하지 않습니다."),

    // 이메일 인증 관련 에러
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "USER400_1", "유효하지 않은 인증 코드입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
