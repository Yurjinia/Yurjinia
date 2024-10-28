package com.yurjinia.platform.auth.dto;

import com.yurjinia.platform.common.validator.PasswordValidate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDTO {

    @PasswordValidate
    private String newPassword;

    @NotBlank
    private String confirmPassword;

}
