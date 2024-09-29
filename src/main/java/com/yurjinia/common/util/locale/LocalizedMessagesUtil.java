package com.yurjinia.common.util.locale;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.yurjinia.common.util.locale.constants.LangConstants.BUNDLE_NAME;

@UtilityClass
public class LocalizedMessagesUtil {

    public static Optional<String> getMessageForLocale(String messageKey, Locale locale, Object... params) {
        ResourceBundle appBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);

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
