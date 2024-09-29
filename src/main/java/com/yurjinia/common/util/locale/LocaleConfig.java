package com.yurjinia.common.util.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import static com.yurjinia.common.util.locale.constants.LangConstants.BASENAME_MESSAGES;
import static com.yurjinia.common.util.locale.constants.LangConstants.UTF_8_ENCODING;

@Configuration
public class LocaleConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames(BASENAME_MESSAGES);
        messageSource.setDefaultEncoding(UTF_8_ENCODING);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CommonLocaleResolver();
    }

}
