package com.yurjinia.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommonException extends RuntimeException {

    private ErrorCode errorCode;
    private List<String> params;
    private HttpStatus status;

    public CommonException(ErrorCode errorCode, HttpStatus status) {
        setErrorCode(errorCode);
        setStatus(status);
        this.params = List.of(errorCode.getDescription());
    }

    public CommonException(ErrorCode errorCode, HttpStatus status, List<String> params) {
        this.params = new ArrayList<>();
        setErrorCode(errorCode);
        setParams(params);
        setStatus(status);
    }

}
