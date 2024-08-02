package com.yurjinia.common.security.jwt.service;

import com.yurjinia.common.security.jwt.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final BlacklistRepository blacklistRepository;

    public void blacklistToken(String token, long expirationTime) {
        blacklistRepository.blacklistToken(token, expirationTime);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepository.isTokenBlacklisted(token);
    }

}

