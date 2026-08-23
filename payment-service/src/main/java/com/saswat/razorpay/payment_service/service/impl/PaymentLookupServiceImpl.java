package com.saswat.razorpay.payment_service.service.impl;

import com.saswat.razorpay.common_lib.enums.PaymentStatus;
import com.saswat.razorpay.payment_service.api.PaymentLookupService;
import com.saswat.razorpay.payment_service.entity.Payment;
import com.saswat.razorpay.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentLookupServiceImpl implements PaymentLookupService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> findUnsettledCapturedPayments(UUID merchantId) {
        return paymentRepository.findByMerchantIdAndStatusForUpdate(merchantId, PaymentStatus.CAPTURED);
    }
}
