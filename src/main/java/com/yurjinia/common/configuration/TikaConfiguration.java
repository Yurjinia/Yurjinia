package com.yurjinia.common.configuration;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TikaConfiguration {

    @Bean
    public Tika tika() {
        return new Tika();
    }

}
