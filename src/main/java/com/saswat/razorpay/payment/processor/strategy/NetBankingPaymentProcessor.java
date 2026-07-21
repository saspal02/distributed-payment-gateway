package com.saswat.razorpay.payment.processor.strategy;

import com.saswat.razorpay.common.util.RandomizerUtil;
import com.saswat.razorpay.payment.processor.PaymentProcessor;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("BANK").toString()  : null;

        if (BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        String redirectRef = "http://REDIRECT_BANK.com/"+ processorRef;

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
