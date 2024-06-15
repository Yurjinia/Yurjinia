package com.yurjinia.auth.controller.request;

import com.yurjinia.common.validator.EmailValidate;
import com.yurjinia.common.validator.PasswordValidate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    @EmailValidate
    private String email;

    @PasswordValidate
    private String password;
}
