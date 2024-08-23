package com.project.lettertome_be.domain.user.jwt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore; //어노테이션 주의...
import lombok.AllArgsConstructor;
import lombok.Getter;

/*인증용 객체랑 인가 전달용 객체를 확실히 분리하기 위해 AuthUser를 사용*/
@Getter
@AllArgsConstructor
public class AuthUser {

    private final Long userId;

    private final String email;

    @JsonIgnore
    private final String password;

}