package com.yurjinia.common.security.jwt.filters;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.constants.JwtConstants;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.common.validator.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${APP.AUTH.URL}")
    public String authUrl;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            if (isRequestSentToPublicEndpoint(request) || isAlreadyAuthenticated()) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractJwtFromRequest(request);

            if (jwtService.isTokenBlacklisted(token)) {
                throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.UNAUTHORIZED);
            }

            UserDetails userDetails = authenticateToken(token);
            setAuthentication(userDetails, request);

            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            handleCommonException(response, ex);
        } catch (Exception ex) {
            handleCommonException(response, new CommonException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, List.of(ex.getMessage())));
        }
    }

    private boolean isRequestSentToPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return isAuthUrl(path);
    }

    private boolean isAuthUrl(String path) {
        return path.startsWith(authUrl);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(requestHeader) && requestHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return requestHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    private UserDetails authenticateToken(String token) throws CommonException {
        if (!isValidToken(token)) {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.UNAUTHORIZED);
        }

        String userEmail = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new CommonException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        return userDetails;
    }

    private boolean isValidToken(String token) {
        return StringUtils.hasText(token) && JwtValidator.isValidateFormat(token);
    }

    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleCommonException(HttpServletResponse response, CommonException ex) throws IOException {
        response.setStatus(ex.getStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"errorCode\": \"%s\",\"status\": \"%s\", \"message\": \"%s\"}",
                ex.getErrorCode(), ex.getStatus().name(), ex.getParams().toString()));
    }

}
