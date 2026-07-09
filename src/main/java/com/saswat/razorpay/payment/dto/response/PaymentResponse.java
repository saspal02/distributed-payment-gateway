package com.saswat.razorpay.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.saswat.razorpay.common.enums.PaymentMethod;
import com.saswat.razorpay.common.enums.PaymentStatus;
import com.saswat.razorpay.common.entity.Money;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        Long refundedAmountPaise,
        LocalDateTime capturedAt,
        LocalDateTime createdAt


) {
}
