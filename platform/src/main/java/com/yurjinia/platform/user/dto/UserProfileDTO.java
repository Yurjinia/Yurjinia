package com.yurjinia.platform.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String avatarId;
    private String username;
}
