package com.project.lettertome_be.domain.user.jwt.service;

import com.project.lettertome_be.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisUtil redisUtil;

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final long BLACKLIST_TTL_MS = 2 * 24 * 3600 * 1000L; // 2일

    //주어진 토큰을 블랙리스트에 추가하고, 설정한 TTL만큼 유지
    public void addToBlacklist(String token) {
        String key = BLACKLIST_PREFIX + token;
        redisUtil.save(key, true, BLACKLIST_TTL_MS, TimeUnit.MILLISECONDS);
    }

    //이메일로 Redis에서 토큰 조회
    public String getRefreshTokenByEmail(String email) {
        return (String) redisUtil.get(email + ":refresh");
    }

    //이메일로 Redis에서 토큰 삭제
    public void deleteTokenByEmail(String email) {
        redisUtil.delete(email + ":refresh");
    }

}
