package com.yurjinia.common.security.jwt.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.constants.JwtConstants;
import com.yurjinia.common.security.jwt.dto.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.token.key}")
    private String secretKey;

    @Value("${jwt.token.expiration}")
    private long expirationInSeconds;

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    public long getExpirationTime(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    private boolean isTokenExpired(String jwt) {
        Date date = new Date();
        return extractExpiration(jwt).before(date);
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String generateToken(String email) {
        Date expirationDate = new Date((new Date()).getTime() + expirationInSeconds * 1000);
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claim) {
        final Claims claims = extractAllClaims(token);
        return claim.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken getCurrentUserToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        if (Objects.isNull(requestAttributes)) {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.BAD_REQUEST);
        }
        String authorization = requestAttributes.getRequest().getHeader(JwtConstants.AUTHORIZATION_HEADER);
        return getUserToken(authorization);
    }

    public JwtToken getUserToken(String token) {
        try {
            token = processBearerToken(token);
            Claims claims = extractAllClaims(token);

            JwtToken userInfo = JwtToken.builder().userName(getValue(claims)).build();
            if (isTokenExpired(token)) {
                throw new CommonException(ErrorCode.JWT_EXPIRED, HttpStatus.BAD_REQUEST);
            }
            return userInfo;

        } catch (Exception exception) {
            if (exception instanceof CommonException) {
                throw exception;
            }

            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.BAD_REQUEST, List.of(exception.getMessage()));
        }
    }

    private String processBearerToken(String token) {
        if (!StringUtils.startsWith(token, JwtConstants.TOKEN_PREFIX)) {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.BAD_REQUEST);
        }
        return StringUtils.substringAfter(token, JwtConstants.TOKEN_PREFIX);
    }

    private String getValue(Claims body) {
        if (Objects.isNull(body.get(JwtConstants.USERNAME_KEY))) {
            return StringUtils.EMPTY;
        }
        return body.get(JwtConstants.USERNAME_KEY).toString();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
