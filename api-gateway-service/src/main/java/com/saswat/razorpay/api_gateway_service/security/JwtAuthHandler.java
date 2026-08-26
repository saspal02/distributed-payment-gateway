package com.saswat.razorpay.api_gateway_service.security;

import com.saswat.razorpay.common_lib.exception.GatewayAuthenticationException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthHandler {

    private final JwtVerifier jwtVerifier;

    public Map<String, String> authenticate(String token) {
        Claims claims;
        try {
            claims = jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new GatewayAuthenticationException("Invalid or expired token");
        }

        return Map.of(
                "X-Merchant-Id", jwtVerifier.extractMerchantId(claims),
                "X-User-Role", jwtVerifier.extractRole(claims)
        );
    }
}
