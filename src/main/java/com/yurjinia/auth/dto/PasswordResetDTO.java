package com.yurjinia.auth.dto;

import com.yurjinia.common.validator.PasswordValidate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDTO {

    @PasswordValidate
    private String newPassword;

    @NotBlank
    private String confirmPassword;

}
