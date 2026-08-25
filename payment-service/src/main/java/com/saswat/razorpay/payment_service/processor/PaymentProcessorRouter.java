package com.saswat.razorpay.payment_service.processor;

import com.saswat.razorpay.common_lib.enums.PaymentMethod;
import com.saswat.razorpay.common_lib.dto.PaymentProcessorRequest;
import com.saswat.razorpay.common_lib.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor processor = paymentProcessors.get(request.method());
        if (processor == null) {
            throw new IllegalArgumentException("No Payment processor registered for method " + request.method());

        }

        return processor.charge(request);

    }
}
