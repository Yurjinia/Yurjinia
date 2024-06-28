package com.yurjinia.common.security.jwt.filters;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.constants.JwtConstants;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.common.validator.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
            if (shouldSkipFilter(request, response, filterChain)) {
                return;
            }

            String token = extractJwtFromRequest(request);
            UserDetails userDetails = authenticateToken(token, request, response, filterChain);
            setAuthentication(userDetails, request);

            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            handleCommonException(response, ex);
        } catch (Exception ex) {
            handleGeneralException(response, ex);
        }
    }

    private boolean shouldSkipFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String path = request.getRequestURI();
        if (isAuthUrl(path)) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean isAuthUrl(String path) {
        return path.startsWith(JwtConstants.AUTH_URL);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(requestHeader) && requestHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return requestHeader.substring(JwtConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    private UserDetails authenticateToken(String token,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain filterChain) throws CommonException, ServletException, IOException {
        if (!isValidToken(token)) {
            throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.UNAUTHORIZED);
        }

        String userEmail = jwtService.extractUsername(token);
        if (isAlreadyAuthenticated()) {
            filterChain.doFilter(request, response);
        }

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

    private void handleGeneralException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"errorCode\": \"%s\", \"message\": \"%s\"}",
                HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

}
