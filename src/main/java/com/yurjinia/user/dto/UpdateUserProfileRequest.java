package com.yurjinia.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UpdateUserProfileRequest {

    @Length(min = 4, max = 16)
    @NotBlank
    private String username;

    private String firstName;
    private String lastName;
}
