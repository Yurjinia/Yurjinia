package com.yurjinia.common.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommonException extends RuntimeException {

    private ErrorCode errorCode;
    private List<String> params;

    public CommonException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.params = List.of(errorCode.getDescription());
    }

    public CommonException(ErrorCode errorCode, List<String> params) {
        this.params = new ArrayList<>();
        setErrorCode(errorCode);
        setParams(params);
    }

}
