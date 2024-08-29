package com.project.lettertome_be.global.jwt.exception;

import com.project.lettertome_be.global.common.response.ApiResponse;
import com.project.lettertome_be.global.common.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityErrorCode errorCode = SecurityErrorCode.FORBIDDEN;

        HttpResponseUtil.setErrorResponse(response, errorCode.getHttpStatus(),
                ApiResponse.onFailure(errorCode.getCode(), "Access is denied"));
    }
}

