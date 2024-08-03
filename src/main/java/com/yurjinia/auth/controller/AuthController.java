package com.yurjinia.auth.controller;

import com.yurjinia.auth.controller.request.LoginRequest;
import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.auth.dto.PasswordResetDTO;
import com.yurjinia.auth.dto.PasswordResetRequest;
import com.yurjinia.auth.service.AuthService;
import com.yurjinia.common.security.jwt.dto.JwtAuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "New user registration",
            description = "This endpoint allows a user to register by passing registration details and an optional profile picture.")
    public JwtAuthenticationResponse signUp(@RequestPart("registrationRequest") RegistrationRequest registrationRequest,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        return authService.signUp(registrationRequest, image);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "User authentication using credentials.")
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest userDTO) {
        return authService.login(userDTO);
    }

    @ResponseBody
    @GetMapping("/login/google")
    @Operation(summary = "Login through Google", description = "Getting user information after login via Google OAuth2.")
    public JwtAuthenticationResponse getLoginInfo(@AuthenticationPrincipal OAuth2User user) {
        return authService.handleOAuthUser(user);
    }

    @PostMapping("/password-reset/request")
    @Operation(summary = "Password reset request", description = "Request to send password reset instructions to email.")
    public void passwordResetRequest(@RequestBody PasswordResetRequest passwordResetRequest) {
        authService.passwordResetRequest(passwordResetRequest);
    }

    @GetMapping("/password-reset/validate")
    @Operation(summary = "Token validation for password reset", description = "Validation of password reset token.")
    public ResponseEntity<Void> validateResetPassword(@RequestParam("token") String token) {
        authService.validateResetPassword(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Password reset", description = "Resetting the user's password using a token and a new password.")
    public void resetPassword(@RequestParam("token") String token, @RequestBody PasswordResetDTO passwordResetDTO) {
        authService.resetPassword(token, passwordResetDTO);
    }

    /*
        ToDo: Refer to next JIRA with having more clarification about the reasons of
         why the code was commented, and when it's going to be uncommented:
         https://pashka1clash.atlassian.net/browse/YUR-114

        @GetMapping("/sign-up/confirm")
        public String confirmSignUp(@RequestParam("token") String token) {
            authService.confirmSignUp(token);
            return "Sign-up confirmed successfully!";
        }

        @PostMapping("/sign-up/resend-confirmation")
        public ResponseEntity<String> resendConfirmation(@RequestBody ResendConfirmationRequest request) {
            try {
                String response = authService.resendConfirmation(request.getEmail());
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    */

}
