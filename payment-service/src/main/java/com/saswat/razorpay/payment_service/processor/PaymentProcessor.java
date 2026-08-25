package com.saswat.razorpay.payment_service.processor;

import com.saswat.razorpay.common_lib.dto.PaymentProcessorRequest;
import com.saswat.razorpay.common_lib.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);


}
