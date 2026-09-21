package com.saswat.paygrid.common_lib.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(OpenApiProperties.class)
public class SharedOpenApiAutoConfiguration {

    private static final String BEARER_SCHEME_NAME = "bearerAuth";
    private static final String BASIC_SCHEME_NAME = "basicAuth";
    private static final String BEARER_FORMAT = "JWT";
    private static final String DEFAULT_VERSION = "v1";

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI sharedOpenAPI(OpenApiProperties properties,
            @Value("${spring.application.name:api}") String applicationName) {
        final String resolvedTitle = resolveTitle(properties.getTitle(), applicationName);
        final String resolvedVersion = resolveVersion(properties.getVersion());
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME_NAME, bearerScheme())
                        .addSecuritySchemes(BASIC_SCHEME_NAME, basicScheme()))
                .info(new Info()
                        .title(resolvedTitle)
                        .description(properties.getDescription())
                        .version(resolvedVersion));
    }

    private SecurityScheme bearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(BEARER_FORMAT)
                .description("Merchant JWT from the login API. Enter the token without the Bearer prefix");
    }

    private SecurityScheme basicScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("API key as Basic auth username and secret as password");
    }

    private String resolveTitle(String configuredTitle, String applicationName) {
        final boolean hasConfiguredTitle = configuredTitle != null && !configuredTitle.isBlank();
        if (hasConfiguredTitle) {
            return configuredTitle;
        }
        final boolean hasApplicationName = applicationName != null && !applicationName.isBlank();
        if (hasApplicationName) {
            return applicationName + " API";
        }
        return "API";
    }

    private String resolveVersion(String configuredVersion) {
        final boolean hasVersion = configuredVersion != null && !configuredVersion.isBlank();
        if (hasVersion) {
            return configuredVersion;
        }
        return DEFAULT_VERSION;
    }
}
