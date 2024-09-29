package com.yurjinia.common.locale;

import com.yurjinia.common.locale.constants.LanguageConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class CommonLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String langParam = request.getParameter(LanguageConstants.LANG_PARAM);

        if (StringUtils.isBlank(langParam)) {
            return request.getLocale();
        }

        return SupportedLanguage.getLocaleByCode(langParam);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

}
