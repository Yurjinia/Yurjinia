package com.yurjinia.common.util.locale;

import java.util.Locale;

public class LocaleCode {

    public static Locale defineLocale(String langParam) {
        if (langParam == null || langParam.isEmpty()) {
            return Locale.ENGLISH;  // Локаль за замовчуванням
        }

        String[] langParts = langParam.split("_");
        if (langParts.length == 1) {
            // Якщо передано тільки мову, наприклад "en"
            return new Locale(langParts[0]);
        } else if (langParts.length == 2) {
            // Якщо передано мову та країну, наприклад "en_US"
            return new Locale(langParts[0], langParts[1]);
        } else {
            // Якщо формат некоректний, повертаємо локаль за замовчуванням
            return Locale.ENGLISH;
        }
    }

}

