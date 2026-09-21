package com.saswat.paygrid.payment_service.repository;

import com.saswat.paygrid.common_lib.enums.OutboxStatus;
import com.saswat.paygrid.payment_service.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
