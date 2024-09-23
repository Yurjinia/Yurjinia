package com.yurjinia.common.util.locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
import java.util.Objects;

public class CommonLocaleResolver implements LocaleResolver {

    private static final Locale ukrainianLocale = new Locale("uk");

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String langParam = request.getParameter("lang");

        if (Objects.equals(langParam, "uk")) {
            return ukrainianLocale;
        }

        return Locale.ENGLISH;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

}
