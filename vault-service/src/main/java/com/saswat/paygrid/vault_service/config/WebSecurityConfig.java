package com.saswat.paygrid.vault_service.config;

import com.saswat.paygrid.common_lib.idempotency.IdempotencyFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final IdempotencyFilter idempotencyFilter;

    @Bean
    public FilterRegistrationBean<Filter> idempotencyFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(idempotencyFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+2);
        registration.addUrlPatterns("/*");
        return registration;
    }

}
