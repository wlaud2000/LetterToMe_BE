package com.project.lettertome_be.global.common.response;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
    ApiResponse<Void> getErrorResponse();

}
