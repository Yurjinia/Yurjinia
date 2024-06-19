package com.yurjinia.common.exception.handling;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse body = ErrorResponse.builder()
                .message(errors.toString())
                .errorCode(ErrorCode.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
