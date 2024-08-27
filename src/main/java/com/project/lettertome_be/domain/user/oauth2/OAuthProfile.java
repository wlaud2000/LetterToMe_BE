package com.project.lettertome_be.domain.user.oauth2;

import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.entity.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class OAuthProfile {  // OAuthProfile 클래스 선언, OAuth 관련 사용자 프로필 정보를 담기 위해 사용

    private String oauthId; // OAuth 제공자가 부여한 고유 ID

    private String nickname;  // 사용자의 닉네임을 저장할 필드

    // OauthProfile 객체를 User 엔티티로 변환하는 메서드
    public User toUser(Provider provider) {
        return User.builder()
                .oauthId(this.oauthId)  // OAuth에서 받은 고유 ID를 설정
                .nickname(this.nickname)  // OAuth에서 받은 닉네임을 설정
                .provider(provider)  // OAuth 제공자 정보를 설정
                .build();
    }

}
