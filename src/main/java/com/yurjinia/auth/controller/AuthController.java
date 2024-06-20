package com.yurjinia.auth.controller;

import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.auth.service.AuthService;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public JwtAuthenticationResponse signUp(@RequestPart("userDTO") UserDTO userDTO,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        return authService.signUp(userDTO, image);
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

    // ToDo: /password-reset/request, rename method appropriately
    @PostMapping("/request-password-reset")
    public void requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
        authService.requestPasswordReset(passwordResetRequest);
    }

    // ToDo: you use here a path variable - '/password-reset/{token}', but when you send the link to user, you send
    //  a request param - '/password-reset?token'.
    //  You must receive a request param.
    // ToDo: also you accept '@RequestBody PasswordResetDTO passwordResetDTO', you need to reset password
    // ToDo: /password-reset/validate, rename method appropriately
    @GetMapping("/password-reset/{token}")
    public void resetPassword(@PathVariable String token, @RequestBody PasswordResetDTO passwordResetDTO) {
        authService.resetPassword(token, passwordResetDTO);
    }

    // ToDo: add endpoint that accepts new password, validates newPassword == confirmNewPassword,
    //  validates newPassword != oldPassword,
    //  and after saving new password we send an email to the user that they
    //  can login.

}
