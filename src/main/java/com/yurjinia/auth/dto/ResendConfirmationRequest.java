package com.yurjinia.auth.dto;

import com.yurjinia.common.validator.EmailValidate;
import lombok.Data;

@Data
public class ResendConfirmationRequest {

    @EmailValidate
    private String email;

}
