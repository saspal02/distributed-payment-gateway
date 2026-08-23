package com.saswat.razorpay.payment_service.statemachine;

import com.saswat.razorpay.common_lib.enums.PaymentActor;
import com.saswat.razorpay.common_lib.enums.PaymentEvent;
import com.saswat.razorpay.common_lib.enums.PaymentStatus;
import com.saswat.razorpay.payment_service.entity.Payment;
import com.saswat.razorpay.payment_service.entity.PaymentTransitionLog;
import com.saswat.razorpay.payment_service.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent  event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM) // TODO:fetch merchant context to identify actor
                .occurredAt(LocalDateTime.now())
                .build();
        payment.setStatus(next);
        log = paymentTransitionLogRepository.save(log);
        return next;
    }
}
