package com.yurjinia.common.utils;

import com.yurjinia.common.locale.constants.LanguageConstants;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@UtilityClass
public class MessageUtils {

    public static Optional<String> getMessageForLocale(String messageKey, Locale locale, Object... params) {
        ResourceBundle appBundle = ResourceBundle.getBundle(LanguageConstants.BUNDLE_NAME, locale);

        if (!appBundle.containsKey(messageKey)) {
            return Optional.empty();
        }

        return getMessage(messageKey, appBundle, params);
    }

    private static Optional<String> getMessage(String messageKey, ResourceBundle bundle, Object[] params) {
        String message = bundle.getString(messageKey);
        String withParams = MessageFormat.format(message, params);

        return Optional.of(withParams);
    }

}
