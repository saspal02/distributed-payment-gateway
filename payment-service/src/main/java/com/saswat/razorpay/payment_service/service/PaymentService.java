package com.saswat.razorpay.payment_service.service;

import com.saswat.razorpay.payment_service.dto.request.PaymentInitRequest;
import com.saswat.razorpay.payment_service.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);

    void resolveAuthorization(UUID paymentId, boolean approve, String bankRef, String errorCode, String errorDescription);
}
