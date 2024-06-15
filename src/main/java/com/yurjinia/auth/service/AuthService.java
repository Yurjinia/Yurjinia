package com.yurjinia.auth.service;

import com.yurjinia.auth.constants.LoginConstants;
import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(UserDTO userDTO) {
        userService.isAuthenticated(userDTO);

        userService.save(userDTO);

        var jwt = jwtService.generateToken(userDTO.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse login(LoginRequest request) {
        isEmailNotExist(request);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        var jwt = jwtService.generateToken(request.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse loginOAuth(LoginRequest request) {
        isEmailNotExist(request);

        var jwt = jwtService.generateToken(request.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse handleOAuthUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute(LoginConstants.EMAIL);
        String firstname = oAuth2User.getAttribute(LoginConstants.GIVEN_NAME);
        String lastName = oAuth2User.getAttribute(LoginConstants.FAMILY_NAME);
        String avatarId = oAuth2User.getAttribute(LoginConstants.PICTURE);

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

    private UserDTO payloadOAuthUser(String email, String firstname, String lastName, String avatarId) {
        return UserDTO.builder()
                .firstName(firstname)
                .lastName(lastName)
                .email(email)
                .password("")
                .avatarId(avatarId)
                .build();
    }

    private void isEmailNotExist(LoginRequest request) {
        if (!userService.existsByEmail(request.getEmail())) {
            throw new CommonException(ErrorCode.EMAIL_NOT_EXISTS, List.of("User by email: " + request.getEmail() + " does not exist"));
        }
    }

}
