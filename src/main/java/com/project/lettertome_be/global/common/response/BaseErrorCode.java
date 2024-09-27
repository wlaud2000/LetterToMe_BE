package com.project.lettertome_be.global.common.response;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
    default ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(getHttpStatus(), getCode(), getMessage());
    }
}
