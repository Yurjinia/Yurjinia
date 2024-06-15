package com.yurjinia.user.dto;

import com.yurjinia.common.validator.EmailValidate;
import com.yurjinia.common.validator.PasswordValidate;
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
public class UserDTO {
    private String firstName;
    private String lastName;
    private String avatarId;

    @Length(min = 4, max = 16)
    private String username;

    @EmailValidate
    private String email;

    @PasswordValidate
    private String password;
}
