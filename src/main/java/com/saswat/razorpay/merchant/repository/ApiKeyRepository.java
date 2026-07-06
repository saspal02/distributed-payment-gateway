package com.saswat.razorpay.merchant.repository;

import com.saswat.razorpay.merchant.dto.response.ApiKeyResponse;
import com.saswat.razorpay.merchant.entity.ApiKey;
import org.aspectj.weaver.Iterators;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    List<ApiKey> findByMerchant_Id(UUID merchantId);

    Optional<ApiKey> findByIdAndMerchant_Id(UUID keyId, UUID merchantId);
}
