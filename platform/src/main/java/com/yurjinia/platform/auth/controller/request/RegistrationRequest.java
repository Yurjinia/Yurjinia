package com.yurjinia.platform.auth.controller.request;

import com.yurjinia.platform.common.validator.EmailValidate;
import com.yurjinia.platform.common.validator.PasswordValidate;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    private String firstName;
    private String lastName;

    @Length(min = 4, max = 16)
    private String username;

    @EmailValidate
    private String email;

    @PasswordValidate
    private String password;

    @NotBlank
    private String confirmPassword;

}
