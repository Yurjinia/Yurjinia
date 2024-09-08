package com.yurjinia.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserProfileRequest {
    private String firstName;
    private String lastName;
}
