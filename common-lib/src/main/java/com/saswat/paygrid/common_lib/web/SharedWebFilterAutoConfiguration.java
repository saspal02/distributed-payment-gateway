package com.saswat.paygrid.common_lib.web;

import com.saswat.paygrid.common_lib.context.MerchantContext;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.RequestContextFilter;

@AutoConfiguration
public class SharedWebFilterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "app.security.trust-inbound-headers", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<Filter> merchantContextRegistration(MerchantContext merchantContext) {
        FilterRegistrationBean<Filter> registration =
                new FilterRegistrationBean<>(new MerchantContextFilter(merchantContext));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> requestContextFilterRegistration() {
        FilterRegistrationBean<Filter> registration =
                new FilterRegistrationBean<>(new RequestContextFilter());

        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
