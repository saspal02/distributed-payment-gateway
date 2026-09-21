package com.saswat.paygrid.payment_service.gateway.dto;

import com.saswat.paygrid.common_lib.entity.Money;
import com.saswat.paygrid.common_lib.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails

) {
}
