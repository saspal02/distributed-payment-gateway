package com.saswat.paygrid.api_gateway_service.security;

import com.saswat.paygrid.api_gateway_service.client.ApiKeyLookupClient;
import com.saswat.paygrid.common_lib.cache.ApiKeyCache;
import com.saswat.paygrid.common_lib.cache.ApiKeyCacheEntry;
import com.saswat.paygrid.common_lib.exception.GatewayAuthenticationException;
import com.saswat.paygrid.common_lib.exception.RateLimitException;
import com.saswat.paygrid.common_lib.ratelimit.RateLimitResult;
import com.saswat.paygrid.common_lib.ratelimit.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthHandler {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String SECRET_VERIFY_PREFIX = "apikey:secret-verified:";
    private static final Duration SECRET_VERIFY_TTL = Duration.ofSeconds(30);
    private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

    private final ApiKeyCache apiKeyCache;
    private final ApiKeyLookupClient apiKeyLookupClient;
    private final RateLimiter rateLimiter;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${app.rate-limit.use-case.api-key.requests-per-minute:60}")
    private int requestsPerMinute;

    public Map<String, String> authenticate(String authHeader, HttpServletResponse response) {
        String[] credentials = decodeBasic(authHeader);

        if (credentials == null) {
            throw new GatewayAuthenticationException("Malformed API key header");
        }

        String keyId = credentials[0];
        String rawSecret = credentials[1];

        ApiKeyCacheEntry entry = apiKeyCache.get(keyId).orElseGet(() -> loadAndCache(keyId));

        if (entry == null || !entry.enabled() || !secretMatches(rawSecret, entry)) {
            throw new GatewayAuthenticationException("Invalid or missing API key");

        }

        RateLimitResult rateLimitResult = rateLimiter.check("apiKey:" + keyId, requestsPerMinute, 60);
        if (!rateLimitResult.isAllowed()) {
            throw new RateLimitException("Too many requests", rateLimitResult.retryAfterSeconds());
        }

        response.setHeader("X-RateLimit-Limit", String.valueOf(requestsPerMinute));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(rateLimitResult.remaining()));

        return Map.of(
                "X-Merchant-Id", entry.merchantId().toString(),
                "X-Environment", entry.environment().name(),
                "X-Key-Id", entry.keyId()
        );
    }

    private ApiKeyCacheEntry loadAndCache(String keyId) {
        try {
            ApiKeyCacheEntry entry = apiKeyLookupClient.findByKeyId(keyId);
            apiKeyCache.put(keyId, entry);
            return entry;
        } catch (Exception e) {
            log.warn("API key lookup failed keyId={}", keyId, e);
            return null;
        }
    }

    private boolean secretMatches(String rawSecret, ApiKeyCacheEntry entry) {
        final String cacheKey = SECRET_VERIFY_PREFIX + entry.keyId() + ":" + entry.keySecretHash() + ":"
                + sha256(rawSecret);

        try {
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cacheKey))) {
                return true;
            }
        } catch (Exception e) {
            log.warn("Secret verification cache read failed, keyId: {}", entry.keyId());
        }

        final boolean matches = BCRYPT.matches(rawSecret, entry.keySecretHash())
                || (entry.isInGracePeriod()
                        && entry.previousKeySecretHash() != null
                        && BCRYPT.matches(rawSecret, entry.previousKeySecretHash()));

        if (matches) {
            try {
                stringRedisTemplate.opsForValue().set(cacheKey, "true", SECRET_VERIFY_TTL);
            } catch (Exception e) {
                log.warn("Secret verification cache put failed, keyId: {}", entry.keyId());
            }
        }

        return matches;
    }

    private String sha256(String value) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }

    private String[] decodeBasic(String header) {
        try {
            String encoded = header.substring(BASIC_PREFIX.length());
            String decoded = new String (Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
            int colon = decoded.indexOf(':');
            if (colon < 1) return null;
            return new String[] {decoded.substring(0, colon), decoded.substring(colon + 1)};
        } catch (Exception e) {
            return null;
        }
    }
}
