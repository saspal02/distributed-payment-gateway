package com.saswat.razorpay.api_gateway_service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatewayAuthFilterTest {

    private final GatewayAuthFilter filter = new GatewayAuthFilter(null, null, null, null);

    @Test
    void shouldNotFilter_docs_paths_ok() {
        // given
        final String[] docsPaths = {
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/v3/api-docs",
                "/merchant-service/v3/api-docs",
                "/payment-service/v3/api-docs",
                "/vault-service/v3/api-docs",
                "/operations-service/v3/api-docs",
                "/swagger-resources/configuration/ui",
                "/webjars/swagger-ui/swagger-ui.css"
        };

        // when
        // then
        for (final String path : docsPaths) {
            assertTrue(filter.shouldNotFilter(request(path)), "Expected skip for " + path);
        }
    }

    @Test
    void shouldNotFilter_business_paths_ko() {
        // given
        final String[] securedPaths = {
                "/v1/auth/login",
                "/v1/merchants",
                "/v1/payments",
                "/actuator/health"
        };

        // when
        // then
        for (final String path : securedPaths) {
            assertFalse(filter.shouldNotFilter(request(path)), "Expected auth for " + path);
        }
    }

    private HttpServletRequest request(final String requestUri) {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(requestUri);
        return request;
    }
}
