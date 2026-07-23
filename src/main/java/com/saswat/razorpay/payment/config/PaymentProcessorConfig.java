package com.saswat.razorpay.payment.config;

import com.saswat.razorpay.common.enums.PaymentMethod;
import com.saswat.razorpay.payment.processor.PaymentProcessor;
import com.saswat.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.saswat.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.saswat.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {
    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor

        );
    }
}
