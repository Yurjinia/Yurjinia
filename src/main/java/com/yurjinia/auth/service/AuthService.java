package com.yurjinia.auth.service;

import com.yurjinia.auth.constants.LoginConstants;
import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.s3.service.AWSS3Service;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.project_structure.project.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.project_structure.project.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AWSS3Service awsS3Service;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;

    public JwtAuthenticationResponse signUp(UserDTO userDTO, MultipartFile image) {
        userService.isAuthenticated(userDTO);

        userService.save(userDTO);

        UserEntity userEntity = userService.getByEmail(userDTO.getEmail());
        Optional<String> urlOpt = awsS3Service.uploadAvatar(userEntity, image);

        if (urlOpt.isPresent()) {
            userEntity.setAvatarId(urlOpt.get());
            userService.save(userEntity);
        }

        final var jwt = jwtService.generateToken(userDTO.getEmail());
        return new JwtAuthenticationResponse(jwt);
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

    public void requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        isEmailNotExist(passwordResetRequest.getEmail());

        String token = confirmationTokenService.createToken(passwordResetRequest.getEmail());
        // ToDo: compare this link and your endpoint you want to use
        String link = "http://localhost:9000/api/v1/auth/password-reset?token=" + token;//ToDo: Resolve the security breach

        emailService.send(passwordResetRequest.getEmail(), emailService.buildForgotPasswordMessage(link));
    }

    @Transactional
    public void resetPassword(String token, PasswordResetDTO passwordResetDTO) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token);
        UserEntity userEntity = userService.getByEmail(confirmationToken.getUserEmail());

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CommonException(ErrorCode.TOKEN_EXPIRED, HttpStatus.GATEWAY_TIMEOUT);
        }

        userEntity.setPassword(new BCryptPasswordEncoder().encode(passwordResetDTO.getPassword()));

        userService.save(userEntity);
        confirmationTokenService.deleteToken(token);
    }

    private JwtAuthenticationResponse loginByEmail(LoginRequest request) {
        isEmailNotExist(request.getEmail());
        return authenticate(request.getEmail(), request.getPassword());
    }

    private JwtAuthenticationResponse loginByUsername(LoginRequest request) {
        UserEntity userEntity = userService.getByUsername(request.getUsername());
        return authenticate(userEntity.getEmail(), request.getPassword());
    }

    public JwtAuthenticationResponse loginOAuth(LoginRequest request) {
        final var jwt = jwtService.generateToken(request.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    private JwtAuthenticationResponse authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        final var jwt = jwtService.generateToken(email);
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
            UserDTO userDTO = payloadOAuthUser(email, firstname, lastName, avatarId);
            return signUp(userDTO);
        }

    }

    private JwtAuthenticationResponse signUp(UserDTO userDTO) {
        userService.isAuthenticated(userDTO);

        userService.save(userDTO);

        var jwt = jwtService.generateToken(userDTO.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    private UserDTO payloadOAuthUser(String email, String firstname, String lastName, String avatarId) {
        return UserDTO.builder()
                .firstName(firstname)
                .lastName(lastName)
                .email(email)
                .password("")
                .avatarId(avatarId)
                .build();
    }

    private void isEmailNotExist(String email) {
        if (!userService.existsByEmail(email)) {
            throw new CommonException(ErrorCode.EMAIL_NOT_EXISTS, HttpStatus.NOT_FOUND, List.of("User by email: " + email + " does not exist"));
        }
    }

}
