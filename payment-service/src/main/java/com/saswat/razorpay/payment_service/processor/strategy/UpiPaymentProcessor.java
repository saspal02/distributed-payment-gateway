package com.saswat.razorpay.payment_service.processor.strategy;

import com.saswat.razorpay.common_lib.util.RandomizerUtil;
import com.saswat.razorpay.payment_service.processor.PaymentProcessor;
import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("vpa").toString()  : null;

        if (VPA_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("UPI_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "UPI_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
