package com.yurjinia.auth.dto;

import com.yurjinia.common.validator.PasswordValidate;
import lombok.Data;

@Data
public class PasswordResetDTO {

    @PasswordValidate
    private String newPassword;

    private String confirmPassword;

}
