package com.yurjinia.common.util.locale;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum SupportedLanguage {

    UKRAINIAN("uk", new Locale("uk")),
    ENGLISH("en", Locale.ENGLISH);

    private final String code;
    private final Locale locale;

    public static Locale getLocaleByCode(String code) {
        for (SupportedLanguage lang : values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang.getLocale();
            }
        }
        return Locale.ENGLISH;
    }

}

