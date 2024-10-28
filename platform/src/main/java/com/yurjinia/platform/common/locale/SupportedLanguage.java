package com.yurjinia.platform.common.locale;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum SupportedLanguage {

    ENGLISH("en", Locale.ENGLISH),
    UKRAINIAN("uk", Locale.of("uk"));

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

