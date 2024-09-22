package com.yurjinia.common.util.locale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocalizedMessages {

    public static Optional<String> getMessageForLocale(String messageKey, Locale locale, Object... params) {
        // Завантажуємо різні ресурси
        ResourceBundle commonBundle = ResourceBundle.getBundle("i18n/common-messages", locale);
        ResourceBundle appBundle = ResourceBundle.getBundle("i18n/messages", locale);

        // Перевіряємо чи існує ключ у ресурсах
        if (appBundle.containsKey(messageKey)) {
            return getMessage(messageKey, appBundle, params);
        } else if (commonBundle.containsKey(messageKey)) {
            return getMessage(messageKey, commonBundle, params);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> getMessage(String messageKey, ResourceBundle bundle, Object[] params) {
        String message = bundle.getString(messageKey);
        String withParams = MessageFormat.format(message, params);
        return Optional.of(withParams);
    }

}
