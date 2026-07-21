package com.saswat.razorpay.payment.processor.strategy;

import com.saswat.razorpay.common.util.RandomizerUtil;
import com.saswat.razorpay.payment.processor.PaymentProcessor;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED = "40000000002";
    public static final String PAN_CARD_EXPIRED = "400000000002";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        String pan = request.pan();

        if (PAN_CARD_DECLINED.equals(pan)) {
            log.warn("Card declined");
            return new PaymentProcessorResponse.Failure("CARD_DECLINED", "CARD declined by bank");
        }

        if (PAN_CARD_EXPIRED.equals(pan)) {
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED", "CARD has expired");

        }

        String processorRef = "CARD_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
