package com.yurjinia.common.security.jwt.service;

import com.yurjinia.common.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final CacheService cacheService;

    public void blacklistToken(String token, long expirationTime) {
        cacheService.blacklistToken(token, expirationTime);
    }

    public boolean isTokenBlacklisted(String token) {
        return cacheService.isTokenBlacklisted(token);
    }

}

