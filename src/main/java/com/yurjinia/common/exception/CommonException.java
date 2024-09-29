package com.yurjinia.common.exception;

import com.yurjinia.common.utils.MessageUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private ErrorCode errorCode;
    private List<String> params;
    private HttpStatus status;

    public CommonException(ErrorCode errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
        this.params = List.of();
    }

    public CommonException(ErrorCode errorCode, HttpStatus status, List<String> params) {
        this.errorCode = errorCode;
        this.status = status;
        this.params = params;
    }

    @Override
    public String getMessage() {
        return getMessageForLocale(Locale.ENGLISH);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessageForLocale(LocaleContextHolder.getLocale());
    }

    private String getMessageForLocale(Locale locale) {
        return MessageUtils
                .getMessageForLocale(errorCode.name(), locale, params)
                .orElse("Localized message for code " + errorCode.name() + " was not found for " + locale.getDisplayName() + " locale");
    }

}
