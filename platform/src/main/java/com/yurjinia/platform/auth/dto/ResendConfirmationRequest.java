package com.yurjinia.platform.auth.dto;

import com.yurjinia.platform.common.validator.EmailValidate;
import lombok.Data;

@Data
public class ResendConfirmationRequest {

    @EmailValidate
    private String email;

}
