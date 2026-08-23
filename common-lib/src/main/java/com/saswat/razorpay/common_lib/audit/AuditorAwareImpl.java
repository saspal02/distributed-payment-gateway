package com.saswat.razorpay.common_lib.audit;


import com.saswat.razorpay.common_lib.context.MerchantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;

    public Optional<String> getCurrentAuditor() {
        try {
            String keyId = merchantContext.getKeyId();
            if (keyId != null && !keyId.isBlank()) return Optional.of(keyId);

            if (merchantContext.getMerchantId() != null) {
                return Optional.of("merchant_id: " + merchantContext.getMerchantId());
            }
        } catch (Exception ignored) {

        }

        return Optional.of("SYSTEM");
    }

}
