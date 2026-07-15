package com.saswat.razorpay.payment.processor.dto;

import com.saswat.razorpay.common.entity.Money;
import com.saswat.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String, Object> methodDetails
) {


}
