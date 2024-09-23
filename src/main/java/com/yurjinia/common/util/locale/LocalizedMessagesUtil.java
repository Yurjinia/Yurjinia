package com.yurjinia.common.util.locale;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@UtilityClass
public class LocalizedMessagesUtil {

    public static Optional<String> getMessageForLocale(String messageKey, Locale locale, Object... params) {
        ResourceBundle appBundle = ResourceBundle.getBundle("i18n/messages", locale);

        if (appBundle.containsKey(messageKey)) {
            return getMessage(messageKey, appBundle, params);
        }

        return Optional.empty();
    }

    private static Optional<String> getMessage(String messageKey, ResourceBundle bundle, Object[] params) {
        String message = bundle.getString(messageKey);
        String withParams = MessageFormat.format(message, params);
        return Optional.of(withParams);
    }

}
