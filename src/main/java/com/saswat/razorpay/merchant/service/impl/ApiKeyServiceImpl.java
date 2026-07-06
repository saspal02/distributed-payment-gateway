package com.saswat.razorpay.merchant.service.impl;

import com.saswat.razorpay.common.exception.ResourceNotFoundException;
import com.saswat.razorpay.common.util.RandomizerUtil;
import com.saswat.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.saswat.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.saswat.razorpay.merchant.dto.response.ApiKeyResponse;
import com.saswat.razorpay.merchant.entity.ApiKey;
import com.saswat.razorpay.merchant.entity.Merchant;
import com.saswat.razorpay.merchant.repository.ApiKeyRepository;
import com.saswat.razorpay.merchant.repository.MerchantRepository;
import com.saswat.razorpay.merchant.service.ApiKeyService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toUpperCase() + RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with BcryptPasswordEncoder
                .environment(request.environment())
                .build();

        apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());

    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(k -> new ApiKeyResponse
                        (k.getId(), k.getKeyId(), k.getEnvironment(), k.isEnabled(),
                                k.getLastUsedAt(), null))
                .toList();
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apikey = apiKeyRepository.findByIdAndMerchant_Id(keyId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        apikey.setEnabled(false);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apikey = apiKeyRepository.findByIdAndMerchant_Id(keyId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));
        String newRawSecret = RandomizerUtil.randomBase64(40);
        apikey.setPreviousKeySecretHash(apikey.getKeySecretHash());
        apikey.setKeySecretHash(newRawSecret); // TODO: encode with bcryptpasswordEncoder
        apikey.setRotatedAt(LocalDateTime.now());
        apikey.setGracePeriodExpiresAt(LocalDateTime.now().plusSeconds(24));
        apiKeyRepository.save(apikey);

        return new ApiKeyCreateResponse(apikey.getId(), apikey.getKeyId(), newRawSecret, apikey.getEnvironment());


    }
}
