package com.yurjinia.auth.service;

import com.yurjinia.auth.controller.request.GoogleLogInRequest;
import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.common.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.common.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${APP.LOGIN_LINK}")
    private String loginLink;

    private final JwtService jwtService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
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

    public JwtAuthenticationResponse login(LoginRequest request, HttpServletRequest httpServletRequest) {
        jwtService.blacklistToken(httpServletRequest);

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

    public JwtAuthenticationResponse handleOAuthUser(GoogleLogInRequest googleLogInRequest) {
        final String email = googleLogInRequest.getEmail();

        if (userService.existsByEmail(email)) {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email(email)
                    .build();
            return loginOAuth(loginRequest);
        } else {
            RegistrationRequest registrationRequest = payloadOAuthUser(email, googleLogInRequest.getFirstName(), googleLogInRequest.getLastName());
            return signUp(registrationRequest, googleLogInRequest.getAvatarId());
        }
    }

    private JwtAuthenticationResponse signUp(RegistrationRequest registrationRequest, String avatarId) {
        userService.createUser(registrationRequest, avatarId);

        final var jwt = jwtService.generateToken(registrationRequest.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    private JwtAuthenticationResponse loginByEmail(LoginRequest request) {
        UserEntity userEntity = userService.getUserByEmail(request.getEmail());
        validatePassword(request.getPassword(), userEntity.getPassword());

        return authenticate(request.getEmail(), request.getPassword());
    }

    private JwtAuthenticationResponse loginByUsername(LoginRequest request) {
        UserEntity userEntity = userService.getByUsername(request.getUsername());
        validatePassword(request.getPassword(), userEntity.getPassword());

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
        UserEntity userEntity = userService.getUserByEmail(tokenEntity.getUserEmail());

        validateNewPasswordIsNotOld(passwordResetDTO.getNewPassword(), userEntity.getPassword());

        userEntity.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));

        userService.save(userEntity);
        confirmationTokenService.deleteToken(token);

        emailService.send(userEntity.getEmail(), emailService.buildForgotPasswordSuccessMessage(loginLink));
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
            throw new CommonException(ErrorCode.PASSWORD_MATCHES_OLD_PASSWORD, HttpStatus.BAD_REQUEST);
        }
    }

    private void isEmailNotExist(String email) {
        if (!userService.existsByEmail(email)) {
            throw new CommonException(ErrorCode.EMAIL_NOT_EXISTS, HttpStatus.NOT_FOUND, List.of("User by email: " + email + " does not exist"));
        }
    }

    private void validatePassword(String requestPassword, String userPassword) {
        if (!passwordEncoder.matches(requestPassword, userPassword)) {
            throw new CommonException(ErrorCode.PASSWORD_IS_NOT_MATCHED, HttpStatus.UNAUTHORIZED);
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
