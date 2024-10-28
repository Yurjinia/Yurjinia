package com.yurjinia.platform.common.exception.handling;

import com.yurjinia.platform.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode errorCode;
    private HttpStatus status;
    private String message;
}
