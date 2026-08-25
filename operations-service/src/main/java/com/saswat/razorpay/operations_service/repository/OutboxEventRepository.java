package com.saswat.razorpay.operations_service.repository;

import com.saswat.razorpay.common_lib.enums.OutboxStatus;
import com.saswat.razorpay.operations_service.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
