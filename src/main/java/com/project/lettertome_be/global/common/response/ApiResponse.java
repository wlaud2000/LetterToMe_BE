package com.project.lettertome_be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"code", "message", "result"}) //JSON으로 변환될 때 필드 순서를 지정
public class ApiResponse<T> {

    private final String code; //HTTP 상태 코드
    private final String message; //응답 메시지
    @JsonInclude(JsonInclude.Include.NON_NULL) //필드 값이 null 이면 JSON 응답에서 제외됨.
    private final T result;

    //기본적으로 200 OK를 사용하는 성공 응답 생성 메서드
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), result);
    }

    //상태 코드를 받아서 사용하는 성공 응답 생성 메서드
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, T result) {
        return new ApiResponse<>(String.valueOf(status.value()), status.getReasonPhrase(), result);
    }

    //실패 응답 생성 메서드 (데이터 포함)
    public static <T> ApiResponse<T> onFailure(String code, String message, T result) {
        return new ApiResponse<>(code, message, result);
    }

    //실패 응답 생성 메서드 (데이터 없음)
    public static <T> ApiResponse<T> onFailure(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}

