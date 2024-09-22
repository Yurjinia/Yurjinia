package com.yurjinia.common.exception;

import com.yurjinia.common.util.locale.LocalizedMessages;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;

@Data
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
        return getMessageForLocale(Locale.ENGLISH);  // Повертає повідомлення за замовчуванням на англійській мові
    }

    @Override
    public String getLocalizedMessage() {
        return getMessageForLocale(LocaleContextHolder.getLocale());  // Повертає повідомлення на основі поточної локалі
    }

    private String getMessageForLocale(Locale locale) {
        return LocalizedMessages
                .getMessageForLocale(errorCode.name(), locale, params.toArray(new String[]{}))
                .orElse("Localized message for code " + errorCode.name() + " was not found for " + locale.getDisplayName() + " locale");
    }
}
