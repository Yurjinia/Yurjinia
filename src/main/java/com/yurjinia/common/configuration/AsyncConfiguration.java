package com.yurjinia.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync(proxyTargetClass = true)//ToDo: check the Async if needed, issue related to injecting email service
public class AsyncConfiguration {
}
