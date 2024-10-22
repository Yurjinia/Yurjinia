package com.yurjinia.platform.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private UserProfileDTO userProfile;
}
