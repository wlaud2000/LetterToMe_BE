package com.project.lettertome_be.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON400", HttpStatus.BAD_REQUEST.getReasonPhrase()),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON401", HttpStatus.UNAUTHORIZED.getReasonPhrase()),
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON403", HttpStatus.FORBIDDEN.getReasonPhrase()),
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON404", HttpStatus.NOT_FOUND.getReasonPhrase()),
    INTERNAL_SERVER_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
    // 유효성 검사
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALID400_0", "잘못된 파라미터 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

