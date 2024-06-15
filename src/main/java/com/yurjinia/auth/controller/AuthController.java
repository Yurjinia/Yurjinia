package com.yurjinia.auth.controller;

import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.service.AuthService;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@Valid @RequestBody UserDTO userDTO) {
        return authService.signUp(userDTO);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest userDTO) {
        return authService.login(userDTO);
    }

    @ResponseBody
    @GetMapping("/login/google")
    public JwtAuthenticationResponse getLoginInfo(@AuthenticationPrincipal OAuth2User user) {
        return authService.handleOAuthUser(user);
    }

}
