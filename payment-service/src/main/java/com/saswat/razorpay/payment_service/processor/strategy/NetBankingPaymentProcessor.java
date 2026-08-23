package com.saswat.razorpay.payment_service.processor.strategy;

import com.saswat.razorpay.common_lib.util.RandomizerUtil;
import com.saswat.razorpay.payment_service.processor.PaymentProcessor;
import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment_service.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("bank").toString()  : null;

        if (BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        String redirectRef = "http://REDIRECT_BANK.com/"+ processorRef;

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
