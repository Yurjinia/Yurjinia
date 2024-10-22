package com.yurjinia.platform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UpdateUserProfileRequest {

    @NotBlank
    @Length(min = 4, max = 16)
    private String username;

    private String firstName;
    private String lastName;

}
