package com.saswat.razorpay.merchant.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    }

    public String generateAccessToken(String email, UUID merchantId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(60*100)))
                .claim("role", role)
                .signWith(getSecretKey())
                .compact()
    }

    public Claims verifyAccessToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
    }

}
