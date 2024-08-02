package com.yurjinia.auth.service;

import com.yurjinia.auth.constants.LoginConstants;
import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.common.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.common.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.constants.JwtConstants;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.common.security.jwt.service.JwtBlacklistService;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.yurjinia.common.application.constants.ApplicationConstants.LOGIN_LINK;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;

    public JwtAuthenticationResponse signUp(RegistrationRequest registrationRequest, MultipartFile image) {
        validateIfUserNotExists(registrationRequest.getEmail());
        validatePasswordMatch(registrationRequest.getConfirmPassword(), registrationRequest.getPassword());

        userService.createUser(registrationRequest, image);

        /* ToDo: Refer to next JIRA with having more clarification about the reasons of
            why the code was commented, and when it's going to be uncommented:
            https://pashka1clash.atlassian.net/browse/YUR-114

            String token = confirmationTokenService.createToken(registrationRequest.getEmail());
            String confirmationLink = "http://localhost:9000/api/v1/auth/sign-up/confirm?token=" + token;
            sendConfirmationEmail(registrationRequest.getEmail(), confirmationLink);
         */

        return authenticate(registrationRequest.getEmail(), registrationRequest.getPassword());
    }

    /**
     * Authenticates a user based on the provided login request. The method checks if the username is provided
     * in the request and calls the corresponding login method. If the username is not provided, it attempts
     * to authenticate the user by email.
     *
     * @param request the {@link LoginRequest} object containing the login credentials. It may contain either
     *                a username or an email, along with the password.
     * @return a {@link JwtAuthenticationResponse} object containing the JWT token and additional user details
     * upon successful authentication.
     */

    public JwtAuthenticationResponse login(LoginRequest request) {
        if (request.getUsername() != null) {
            return loginByUsername(request);
        } else {
            return loginByEmail(request);
        }
    }

    public JwtAuthenticationResponse loginOAuth(LoginRequest request) {
        final var jwt = jwtService.generateToken(request.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse handleOAuthUser(OAuth2User oAuth2User) {
        final String email = oAuth2User.getAttribute(LoginConstants.EMAIL);
        final String firstname = oAuth2User.getAttribute(LoginConstants.GIVEN_NAME);
        final String lastName = oAuth2User.getAttribute(LoginConstants.FAMILY_NAME);
        final String avatarId = oAuth2User.getAttribute(LoginConstants.PICTURE);

        if (userService.existsByEmail(email)) {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .build();
            return loginOAuth(loginRequest);
        } else {
            RegistrationRequest registrationRequest = payloadOAuthUser(email, firstname, lastName);
            return signUp(registrationRequest, avatarId);
        }
    }

    private JwtAuthenticationResponse signUp(RegistrationRequest registrationRequest, String avatarId) {
        userService.createUser(registrationRequest, avatarId);

        final var jwt = jwtService.generateToken(registrationRequest.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    private JwtAuthenticationResponse loginByEmail(LoginRequest request) {
        isEmailNotExist(request.getEmail());
        return authenticate(request.getEmail(), request.getPassword());
    }

    private JwtAuthenticationResponse loginByUsername(LoginRequest request) {
        UserEntity userEntity = userService.getByUsername(request.getUsername());
        return authenticate(userEntity.getEmail(), request.getPassword());
    }

    private JwtAuthenticationResponse authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        final var jwt = jwtService.generateToken(email);
        return new JwtAuthenticationResponse(jwt);
    }

    private RegistrationRequest payloadOAuthUser(String email, String firstname, String lastName) {
        return RegistrationRequest.builder()
                .firstName(firstname)
                .lastName(lastName)
                .email(email)
                .password("")
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String requestHeader = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(requestHeader) && requestHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            String token = requestHeader.substring(JwtConstants.TOKEN_PREFIX.length());
            long expirationTime = jwtService.getExpirationTime(token);
            var auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(null);

                if (jwtBlacklistService.isTokenBlacklisted(token)) {
                    throw new CommonException(ErrorCode.JWT_INVALID, HttpStatus.UNAUTHORIZED);
                }

                jwtBlacklistService.blacklistToken(token, expirationTime);
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
        }
    }

    public void passwordResetRequest(PasswordResetRequest passwordResetRequest) {
        isEmailNotExist(passwordResetRequest.getEmail());

        String token = confirmationTokenService.createToken(passwordResetRequest.getEmail());
        String link = "http://localhost:9000/api/v1/auth/password-reset/validate?token=" + token;

        emailService.send(passwordResetRequest.getEmail(), emailService.buildForgotPasswordMessage(link));
    }

    public void validateResetPassword(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CommonException(ErrorCode.TOKEN_EXPIRED, HttpStatus.GATEWAY_TIMEOUT);
        }
    }

    @Transactional
    public void resetPassword(String token, PasswordResetDTO passwordResetDTO) {
        validatePasswordMatch(passwordResetDTO.getConfirmPassword(), passwordResetDTO.getNewPassword());

        ConfirmationTokenEntity tokenEntity = confirmationTokenService.getToken(token);
        UserEntity userEntity = userService.getByEmail(tokenEntity.getUserEmail());

        validateNewPasswordIsNotOld(passwordResetDTO.getNewPassword(), userEntity.getPassword());

        userEntity.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));

        userService.save(userEntity);
        confirmationTokenService.deleteToken(token);

        emailService.send(userEntity.getEmail(), emailService.buildForgotPasswordSuccessMessage(LOGIN_LINK));
    }

    private void validateIfUserNotExists(String email) {
        userService.findByEmail(email).ifPresent(existingUser -> {
            if (existingUser.isEnabled()) {
                throw new CommonException(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
            } else {
                throw new CommonException(
                        ErrorCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT,
                        List.of("User with this email already exists but email is not confirmed. Please confirm an email.")
                );
            }
        });
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!confirmPassword.equals(password)) {
            throw new CommonException(ErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateNewPasswordIsNotOld(String newPassword, String oldPassword) {
        if (passwordEncoder.matches(newPassword, oldPassword)) {
            throw new CommonException(ErrorCode.MATCHES_OLD_PASSWORD, HttpStatus.BAD_REQUEST);
        }
    }

    private void isEmailNotExist(String email) {
        if (!userService.existsByEmail(email)) {
            throw new CommonException(ErrorCode.EMAIL_NOT_EXISTS, HttpStatus.NOT_FOUND, List.of("User by email: " + email + " does not exist"));
        }
    }

    /* ToDo: Refer to next JIRA with having more clarification about the reasons of
        why the code was commented, and when it's going to be uncommented:
        https://pashka1clash.atlassian.net/browse/YUR-114

        public String resendConfirmation(String email) {
            UserEntity user = userService.getByEmail(email);
            if (user.isActive()) {
                throw new CommonException(ErrorCode.USER_ALREADY_ACTIVE, HttpStatus.CONFLICT);
            }

            String token = confirmationTokenService.createToken(email);
            String confirmationLink = "http://localhost:9000/api/v1/auth/sign-up/confirm?token=" + token;
            sendConfirmationEmail(email, confirmationLink);

            return "Confirmation email resent";
        }

        @Transactional
        public void confirmSignUp(String token) {
            ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token);

            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Token expired");
            }

            userService.activateUser(confirmationToken.getUserEmail());

            confirmationTokenService.deleteToken(token);
        }

        private void sendConfirmationEmail(String email, String confirmationLink) {
            emailService.send(email, emailService.buildConfirmationEmailMessage(confirmationLink));
        }

    */

}
