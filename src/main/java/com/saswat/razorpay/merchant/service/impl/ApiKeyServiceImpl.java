package com.saswat.razorpay.merchant.service.impl;

import com.saswat.razorpay.common.exception.ResourceNotFoundException;
import com.saswat.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.saswat.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.saswat.razorpay.merchant.entity.ApiKey;
import com.saswat.razorpay.merchant.entity.Merchant;
import com.saswat.razorpay.merchant.repository.ApiKeyRepository;
import com.saswat.razorpay.merchant.repository.MerchantRepository;
import com.saswat.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {


    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toUpperCase() + "big random string";
        String rawSecret = "secret"; // TODO: replace with cryptography

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret)
                .environment(request.environment())
                .build();

        apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());

    }
}
