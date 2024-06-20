package com.yurjinia.auth.dto;

import com.yurjinia.common.validator.EmailValidate;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @EmailValidate // ToDo: do indent from the top and below
    private String email;
}
