package com.saswat.razorpay.payment_service.gateway;

import com.saswat.razorpay.payment_service.gateway.dto.PaymentRequest;
import com.saswat.razorpay.payment_service.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}
