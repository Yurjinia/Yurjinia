package com.yurjinia.auth.controller;

import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.auth.service.AuthService;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JwtAuthenticationResponse signUp(@RequestPart("registrationRequest") RegistrationRequest registrationRequest,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        return authService.signUp(registrationRequest, image);
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

    @PostMapping("/password-reset/request")
    public void passwordResetRequest(@RequestBody PasswordResetRequest passwordResetRequest) {
        authService.passwordResetRequest(passwordResetRequest);
    }

    @GetMapping("/password-reset/validate")
    public ResponseEntity<Void> validateResetPassword(@RequestParam("token") String token) {
        authService.validateResetPassword(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-reset")
    public void resetPassword(@RequestParam("token") String token, @RequestBody PasswordResetDTO passwordResetDTO) {
        authService.resetPassword(token, passwordResetDTO);
    }

}
