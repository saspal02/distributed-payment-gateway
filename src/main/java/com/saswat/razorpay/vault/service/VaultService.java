package com.saswat.razorpay.vault.service;

import com.saswat.razorpay.common.entity.Money;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.saswat.razorpay.vault.dto.request.TokenizeRequest;
import com.saswat.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
