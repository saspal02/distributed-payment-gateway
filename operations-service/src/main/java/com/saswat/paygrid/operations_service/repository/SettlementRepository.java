package com.saswat.paygrid.operations_service.repository;

import com.saswat.paygrid.common_lib.enums.SettlementStatus;
import com.saswat.paygrid.operations_service.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SettlementRepository extends JpaRepository<Settlement, UUID> {

    List<Settlement> findByStatus(SettlementStatus settlementStatus);
}
