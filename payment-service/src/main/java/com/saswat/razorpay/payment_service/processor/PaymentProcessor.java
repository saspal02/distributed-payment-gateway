package com.saswat.razorpay.payment_service.processor;

import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);


}
