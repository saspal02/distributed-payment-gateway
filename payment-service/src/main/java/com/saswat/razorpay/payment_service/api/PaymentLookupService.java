package com.saswat.razorpay.payment_service.api;


import com.saswat.razorpay.common_lib.dto.PaymentSettlementView;
import com.saswat.razorpay.payment_service.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentLookupService {

    List<PaymentSettlementView> findUnsettledCapturedPayments(UUID merchantId);

    void markSettled(List<UUID> paymentList);

}
