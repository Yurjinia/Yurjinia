package com.yurjinia.platform.auth.controller.request;

import lombok.Getter;

@Getter
public class GoogleLogInRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String avatarId;
}
