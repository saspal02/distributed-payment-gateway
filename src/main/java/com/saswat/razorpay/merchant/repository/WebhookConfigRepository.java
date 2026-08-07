package com.saswat.razorpay.merchant.repository;

import com.saswat.razorpay.merchant.entity.MerchantWebHookConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WebhookConfigRepository  extends JpaRepository<MerchantWebHookConfig, UUID> {

    List<MerchantWebHookConfig> findByMerchant_Id(UUID merchantId);

    Optional<MerchantWebHookConfig> findByIdAndMerchant_Id(UUID configId, UUID merchantId);

    List<MerchantWebHookConfig> findByMerchant_IdAndEnabledTrue(UUID merchantId);
}
