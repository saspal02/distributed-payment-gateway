package com.saswat.razorpay.api_gateway_service.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.security")
public class SecurityRouterProperties {

    private List<String> publicRoutes = List.of();
}
