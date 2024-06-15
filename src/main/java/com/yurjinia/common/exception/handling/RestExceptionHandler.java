package com.yurjinia.common.exception.handling;

import com.yurjinia.common.exception.CommonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(CommonException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message(ex.getParams().toString())
                .errorCode(ex.getErrorCode())
                .status(ex.getStatus())
                .build();

        return new ResponseEntity<>(body, ex.getStatus());
    }

}
