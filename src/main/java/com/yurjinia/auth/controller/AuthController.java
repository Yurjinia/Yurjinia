package com.yurjinia.auth.controller;

import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.service.AuthService;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
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

}
