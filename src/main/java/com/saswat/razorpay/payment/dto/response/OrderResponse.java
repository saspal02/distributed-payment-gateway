package com.saswat.razorpay.payment.dto.response;

import com.saswat.razorpay.common.enums.OrderStatus;
import com.saswat.razorpay.common.money.Money;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID merchantId,
        String receipt,
        Money amount,
        OrderStatus status,
        Integer attempts,
        Map<String, Object> notes,
        LocalDateTime expiresAt,
        LocalDateTime createdAt

) {
}
