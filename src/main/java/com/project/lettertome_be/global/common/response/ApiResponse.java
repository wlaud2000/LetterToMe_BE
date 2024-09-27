package com.project.lettertome_be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

// 생성자로 객체를 생성하는 것을 막기
@AllArgsConstructor(access = AccessLevel.PRIVATE)
// json 형식으로 줄 때 어떤 순서로, 어떤 변수들을 줄것인지 결정하는 Annotation
@JsonPropertyOrder({"isSuccess", "status", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess") // isSuccess라는 변수라는 것을 명시하는 Annotation
    private boolean isSuccess;

    @JsonProperty("status")
    private HttpStatus httpStatus;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) //필드 값이 null 이면 JSON 응답에서 제외됨.
    private final T result;

    // Jackson이 자동으로 Boolean 필드를 잘못 직렬화하는 문제때문에 result에만 따로 get메소드를 만들어줌.
    public T getResult() {
        return result;
    }

    //기본적으로 200 OK를 사용하는 성공 응답 생성 메서드
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, HttpStatus.OK, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), result);
    }

    //상태 코드를 받아서 사용하는 성공 응답 생성 메서드
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, T result) {
        return new ApiResponse<>(true, status, String.valueOf(status.value()), status.getReasonPhrase(), result);
    }

    //실패 응답 생성 메서드 (데이터 포함)
    public static <T> ApiResponse<T> onFailure(HttpStatus status, String code, String message, T result) {
        return new ApiResponse<>(false, status, code, message, result);
    }

    //실패 응답 생성 메서드 (데이터 없음)
    public static <T> ApiResponse<T> onFailure(HttpStatus status, String code, String message) {
        return new ApiResponse<>(false, status, code, message, null);
    }

}

