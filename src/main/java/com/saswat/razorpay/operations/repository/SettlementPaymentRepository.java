package com.saswat.razorpay.operations.repository;

import com.saswat.razorpay.operations.entity.SettlementPayment;
import com.saswat.razorpay.operations.entity.SettlementPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementPaymentRepository extends JpaRepository<SettlementPayment, SettlementPaymentId> {
}
