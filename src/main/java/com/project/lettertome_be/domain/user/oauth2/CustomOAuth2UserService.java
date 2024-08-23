package com.project.lettertome_be.domain.user.oauth2;

import com.project.lettertome_be.domain.user.entity.enums.Provider;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth 서비스(구글, 네이버, 카카오 등)에서 가져온 유저 정보를 담고 있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth 서비스 이름(ex: google, naver, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth 로그인 시 키 값(예: 구글은 "sub", 네이버는 "id", 카카오는 "id")
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 서비스에서 가져온 유저 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // registrationId에 따라 유저 정보를 OauthProfile 객체로 변환
        OAuthProfile oauthProfile = OAuthAttributes.extract(registrationId, attributes);

        // 변환된 OauthProfile 정보를 기반으로 사용자 정보 저장 또는 업데이트
        saveOrUpdateOauthProfile(oauthProfile, Provider.valueOf(registrationId.toUpperCase()));

        // DefaultOAuth2User 객체 생성 후 반환
        return createDefaultOAuth2User(attributes, userNameAttributeName);
    }

    private void saveOrUpdateOauthProfile(OAuthProfile oauthProfile, Provider provider) {
        // oauthId로 사용자 정보를 조회, 존재하면 닉네임 업데이트, 없으면 새로 저장
        userRepository.findByOauthId(oauthProfile.getOauthId())
                .map(existingUser -> existingUser.updateNickname(oauthProfile.getNickname()))  // 기존 사용자 정보가 있을 때 닉네임 업데이트
                .orElseGet(() -> userRepository.save(oauthProfile.toUser(provider)));  // 없으면 새 사용자 저장
    }

    private OAuth2User createDefaultOAuth2User(Map<String, Object> attributes,
                                               String userNameAttributeName) {
        // DefaultOAuth2User 객체 생성 후 반환 (권한 처리 없이)
        return new DefaultOAuth2User(
                Collections.emptyList(),  // 권한 리스트는 비어 있음
                attributes,  // OAuth2 유저 정보 맵
                userNameAttributeName  // 유저의 고유 식별자 속성 이름
        );
    }
}