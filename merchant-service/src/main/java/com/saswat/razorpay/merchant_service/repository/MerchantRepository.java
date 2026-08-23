package com.saswat.razorpay.merchant_service.repository;

import com.saswat.razorpay.common_lib.enums.MerchantStatus;
import com.saswat.razorpay.merchant_service.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    boolean existsByEmail(String email);

    List<Merchant> findByStatus(MerchantStatus status);


}
