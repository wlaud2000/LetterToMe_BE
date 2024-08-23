package com.project.lettertome_be.domain.user.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.Function;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public enum OAuthAttributes {

    NAVER("naver", attributes -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String oauthId = (String) response.get("id");
        String nickname = (String) response.get("name");
        return new OAuthProfile(oauthId, nickname);
    }),

    KAKAO("kakao", attributes -> {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String oauthId = String.valueOf(attributes.get("id"));  // 카카오에서 제공하는 유저의 고유 ID
        String nickname = (String) profile.get("nickname");
        return new OAuthProfile(oauthId, nickname);
    });

    private final String registrationId;
    private final Function<Map<String, Object>, OAuthProfile> oauthProfileFactory;

    public static OAuthProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid registrationId: " + registrationId))
                .oauthProfileFactory.apply(attributes);
    }
}
