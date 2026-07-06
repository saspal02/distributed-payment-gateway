package com.saswat.razorpay.payment.dto.request;

import com.saswat.razorpay.common.money.Money;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        Money amount,
        String receipt, //order-id (known to merchant)
        Map<String, Object> notes,
        LocalDateTime expiresAt
)
{
}
