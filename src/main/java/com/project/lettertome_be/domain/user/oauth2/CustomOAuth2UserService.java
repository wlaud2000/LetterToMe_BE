package com.project.lettertome_be.domain.user.oauth2;

import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.entity.enums.Provider;
import com.project.lettertome_be.domain.user.jwt.userdetails.CustomUserDetails;
import com.project.lettertome_be.domain.user.jwt.util.JwtUtil;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth 서비스(구글, 네이버, 카카오 등)에서 가져온 유저 정보를 담고 있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        /*Name: [{id=asdfqwer, name=김지명}], Granted Authorities: [[OAUTH2_USER]],
        User Attributes: [{resultcode=00, message=success, response={id=asdfqwer, name=김지명}}]*/
        log.info("소셜 로그인 페이지에서 넘어온 정보 -> {}",oAuth2User);

        // OAuth 서비스 이름(ex: google, naver, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth 로그인 시 키 값(예: 구글은 "sub", 네이버는 "id", 카카오는 "id")
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 서비스에서 가져온 유저 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("Attributes -> {}", attributes);

        // attributes 맵을 수정 가능한 맵으로 복사
        Map<String, Object> modifiableAttributes = new HashMap<>(attributes);

        // registrationId에 따라 유저 정보를 OauthProfile 객체로 변환
        OAuthProfile oauthProfile = OAuthAttributes.getProfileByRegistrationId(registrationId, modifiableAttributes);
        log.info("oauthProfile -> {}", oauthProfile);

        // 변환된 OauthProfile 정보를 기반으로 사용자 정보 저장 또는 업데이트
        User user = saveOrUpdateOauthProfile(oauthProfile, Provider.valueOf(registrationId.toUpperCase()));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtUtil.createJwtAccessToken(userDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(userDetails);

        // 로그로 토큰 출력
        log.info("Access Token: {}", accessToken);
        log.info("Refresh Token: {}", refreshToken);

        // 수정 가능한 맵에 토큰 추가
        modifiableAttributes.put("accessToken", accessToken);
        modifiableAttributes.put("refreshToken", refreshToken);

        // DefaultOAuth2User 객체 생성 후 반환
        /*Name: [{id=asdfqwer, name=김지명}], Granted Authorities: [[]],
        User Attributes: [{resultcode=00, message=success, accessToken=토큰, refreshToken=토큰, response={id=asdfqwer, name=김지명}}]*/
        log.info("[ CustomOAuth2UserService ] DefaultOAuth2User : {}", createDefaultOAuth2User(modifiableAttributes, userNameAttributeName));
        return createDefaultOAuth2User(modifiableAttributes, userNameAttributeName);
    }

    private User saveOrUpdateOauthProfile(OAuthProfile oauthProfile, Provider provider) {
        // oauthId로 사용자 정보를 조회, 존재하면 닉네임 업데이트, 없으면 새로 저장
        return userRepository.findByOauthId(oauthProfile.getOauthId())
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