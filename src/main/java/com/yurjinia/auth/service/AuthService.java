package com.yurjinia.auth.service;

import com.yurjinia.auth.constants.LoginConstants;
import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(RegistrationRequest registrationRequest, MultipartFile image) {
        userService.createUser(registrationRequest, image);

        final var jwt = jwtService.generateToken(registrationRequest.getEmail());
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
            RegistrationRequest registrationRequest = payloadOAuthUser(email, firstname, lastName);
            return signUp(registrationRequest, avatarId);
        }

    }

    private JwtAuthenticationResponse signUp(RegistrationRequest registrationRequest, String avatarId) {
        userService.createUser(registrationRequest, avatarId);

        final var jwt = jwtService.generateToken(registrationRequest.getEmail());
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

    private void isEmailNotExist(String email) {
        if (!userService.existsByEmail(email)) {
            throw new CommonException(ErrorCode.EMAIL_NOT_EXISTS, HttpStatus.NOT_FOUND, List.of("User by email: " + email + " does not exist"));
        }
    }

}
