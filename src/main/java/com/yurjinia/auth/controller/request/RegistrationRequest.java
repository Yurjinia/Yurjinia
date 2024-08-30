package com.yurjinia.auth.controller.request;

import com.yurjinia.common.validator.EmailValidate;
import com.yurjinia.common.validator.PasswordValidate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
