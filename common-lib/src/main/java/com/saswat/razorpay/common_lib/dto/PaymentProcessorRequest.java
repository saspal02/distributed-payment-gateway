package com.saswat.razorpay.common_lib.dto;


import com.saswat.razorpay.common_lib.entity.Money;
import com.saswat.razorpay.common_lib.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID processingId,
        UUID paymentId,
        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String, Object> methodDetails
) {

    public static PaymentProcessorRequest card(UUID paymentId, String pan, String expiry, Money amount, Map<String, Object> details) {
        return new PaymentProcessorRequest(UUID.randomUUID(), paymentId, PaymentMethod.CARD, amount,
                pan, expiry, details);
    }

    public static PaymentProcessorRequest nonCard(UUID paymentId, PaymentMethod method, Money amount, Map<String, Object> details) {
        return new PaymentProcessorRequest(UUID.randomUUID(), paymentId, method, amount,
                null, null, details);
    }


}
