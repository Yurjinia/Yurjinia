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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            String path = request.getRequestURI();
            if (isAuthUrl(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = getJwt(request);
            validateTokenAndSetAuthentication(token, request);

            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            handleCommonException(response, ex);

        } catch (Exception ex) {
            handleException(response, ex);
        }
    }

    private boolean isAuthUrl(String path) {
        return path.startsWith(JwtConstants.AUTH_URL);
    }

    private void validateTokenAndSetAuthentication(String token, HttpServletRequest request) throws CommonException {
        if (StringUtils.hasText(token) && JwtValidator.isValidateFormat(token)) {
            final String userEmail = jwtService.extractUsername(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(token, userDetails)) {
                    setAuthentication(userDetails, request);
                } else {
                    throw new CommonException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
                }
            } else {
                throw new CommonException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.UNAUTHORIZED);
        }
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

    private String getJwt(HttpServletRequest request) {
        String requestHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(requestHeader) && requestHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return requestHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    private void handleCommonException(HttpServletResponse response, CommonException ex) throws IOException {
        response.setStatus(ex.getStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"errorCode\": \"%s\",\"status\": \"%s\", \"message\": \"%s\"}", ex.getErrorCode(), ex.getStatus().name(), ex.getParams().toString()));
    }

    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"errorCode\": \"%s\", \"message\": \"%s\"}",
                HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

}

