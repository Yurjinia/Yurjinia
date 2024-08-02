package com.yurjinia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class YurjiniaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YurjiniaApplication.class, args);
    }

}
