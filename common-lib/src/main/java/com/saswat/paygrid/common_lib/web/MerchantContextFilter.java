package com.saswat.paygrid.common_lib.web;

import com.saswat.paygrid.common_lib.context.MerchantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class MerchantContextFilter extends OncePerRequestFilter {

    public static final String MERCHANT_ID_HEADER = "X-Merchant-Id";
    public static final String KEY_ID_HEADER = "X-Key-Id";

    private final MerchantContext merchantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String merchantIdHeader = request.getHeader(MERCHANT_ID_HEADER);
        if (merchantIdHeader != null && !merchantIdHeader.isBlank()) {
            merchantContext.setMerchantId(UUID.fromString(merchantIdHeader));
        }

        String keyId = request.getHeader(KEY_ID_HEADER);
        if (keyId != null && !keyId.isBlank()) {
            merchantContext.setKeyId(keyId);
        }

        filterChain.doFilter(request, response);

    }
}
