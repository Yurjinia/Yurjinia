package com.yurjinia.common.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@Configuration
public class SwaggerConfiguration {

    public SwaggerConfiguration(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public_api")
                .pathsToMatch("/api/v1/auth/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Public API Documentation").version("1.0")))
                .build();
    }

    @Bean
    GroupedOpenApi privateApi() {
        return GroupedOpenApi.builder()
                .group("private_api")
                .pathsToMatch("/api/v1/users/**", "/api/v1/projects/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("Private API Documentation").version("1.0"))
                            .components(new Components()
                                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")))
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                })
                .build();
    }

}

