package com.saswat.razorpay.vault_service.service;

import com.saswat.razorpay.common_lib.entity.Money;
import com.saswat.razorpay.common_lib.dto.PaymentProcessorResponse;
import com.saswat.razorpay.vault_service.dto.request.TokenizeRequest;
import com.saswat.razorpay.vault_service.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
