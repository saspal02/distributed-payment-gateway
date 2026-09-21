package com.saswat.paygrid.merchant_service.repository;

import com.saswat.paygrid.common_lib.enums.MerchantStatus;
import com.saswat.paygrid.merchant_service.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    boolean existsByEmail(String email);

    List<Merchant> findByStatus(MerchantStatus status);


}
