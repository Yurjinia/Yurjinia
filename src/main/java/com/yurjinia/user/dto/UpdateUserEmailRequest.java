package com.yurjinia.user.dto;

import com.yurjinia.common.validator.EmailValidate;
import lombok.Getter;

@Getter
public class UpdateUserEmailRequest {

    @EmailValidate
    private String email;

}
