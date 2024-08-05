package com.yurjinia.common.handlers;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.constants.JwtConstants;
import com.yurjinia.common.security.jwt.service.JwtBlacklistService;
import com.yurjinia.common.security.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final JwtService jwtService;

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String requestHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(requestHeader) || !requestHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.BAD_REQUEST);
        }

        String token = requestHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        long expirationTime = jwtService.getExpirationTime(token);

        if (jwtService.isTokenBlacklisted(token)) {
            throw new CommonException(ErrorCode.JWT_EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        jwtService.blacklistToken(token, expirationTime);
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }

}
