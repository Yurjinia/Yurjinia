package com.yurjinia.platform.user.dto;

import com.yurjinia.platform.common.validator.EmailValidate;
import lombok.Getter;

@Getter
public class UpdateUserEmailRequest {

    @EmailValidate
    private String email;

}
