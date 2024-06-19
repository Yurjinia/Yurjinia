package com.yurjinia.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UsernameDTO {
    @Length(min = 4, max = 16)
    @NotBlank
    private String username;
}
