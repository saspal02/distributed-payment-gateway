package com.saswat.razorpay.merchant.service.impl;

import com.saswat.razorpay.common.exception.ResourceNotFoundException;
import com.saswat.razorpay.common.util.RandomizerUtil;
import com.saswat.razorpay.merchant.cache.ApiKeyCache;
import com.saswat.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.saswat.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.saswat.razorpay.merchant.dto.response.ApiKeyResponse;
import com.saswat.razorpay.merchant.entity.ApiKey;
import com.saswat.razorpay.merchant.entity.Merchant;
import com.saswat.razorpay.merchant.mapper.ApiKeyMapper;
import com.saswat.razorpay.merchant.repository.ApiKeyRepository;
import com.saswat.razorpay.merchant.repository.MerchantRepository;
import com.saswat.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApiKeyMapper apiKeyMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApiKeyCache apiKeyCache;


    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toLowerCase() + RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(passwordEncoder.encode(rawSecret))
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());

    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apikey = apiKeyRepository.findByIdAndMerchant_Id(keyId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        apikey.setEnabled(false);
        apiKeyCache.evict(apikey.getKeyId());
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apikey = apiKeyRepository.findByIdAndMerchant_Id(keyId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));
        String newRawSecret = RandomizerUtil.randomBase64(40);
        apikey.setPreviousKeySecretHash(apikey.getKeySecretHash());
        apikey.setKeySecretHash(passwordEncoder.encode(newRawSecret));
        apikey.setRotatedAt(LocalDateTime.now());
        apikey.setGracePeriodExpiresAt(LocalDateTime.now().plusSeconds(24));
        apiKeyRepository.save(apikey);

        apiKeyCache.evict(apikey.getKeyId());

        return new ApiKeyCreateResponse(apikey.getId(), apikey.getKeyId(), newRawSecret, apikey.getEnvironment());


    }
}
