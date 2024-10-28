package com.yurjinia.platform.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {

    private String username;
    private String email;

    @NotBlank
    private String password;

}
