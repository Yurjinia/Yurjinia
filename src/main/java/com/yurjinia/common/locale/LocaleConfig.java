package com.yurjinia.common.locale;

import com.yurjinia.common.locale.constants.LanguageConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class LocaleConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames(LanguageConstants.BASENAME_MESSAGES);
        messageSource.setDefaultEncoding(LanguageConstants.UTF_8_ENCODING);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CommonLocaleResolver();
    }

}
