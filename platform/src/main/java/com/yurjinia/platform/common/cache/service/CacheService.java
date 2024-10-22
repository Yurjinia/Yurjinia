package com.yurjinia.platform.common.cache.service;

import com.yurjinia.platform.common.cache.constants.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(CacheConstants.BLACKLIST_PREFIX + token, CacheConstants.BLACKLISTED_STATUS, expirationTime, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(CacheConstants.BLACKLIST_PREFIX + token);
    }

}
