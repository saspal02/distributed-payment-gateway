package com.saswat.razorpay.payment_service.saga;

import com.saswat.razorpay.common_lib.enums.EventAggregateType;
import com.saswat.razorpay.common_lib.enums.OrderStatus;
import com.saswat.razorpay.common_lib.enums.PaymentEvent;
import com.saswat.razorpay.common_lib.enums.PaymentStatus;
import com.saswat.razorpay.common_lib.exception.BusinessRuleViolationException;
import com.saswat.razorpay.common_lib.exception.ResourceNotFoundException;
import com.saswat.razorpay.payment_service.dto.request.PaymentInitRequest;
import com.saswat.razorpay.payment_service.dto.response.PaymentResponse;
import com.saswat.razorpay.payment_service.entity.OrderRecord;
import com.saswat.razorpay.payment_service.entity.Payment;
import com.saswat.razorpay.payment_service.gateway.dto.PaymentResult;
import com.saswat.razorpay.payment_service.mapper.PaymentMapper;
import com.saswat.razorpay.payment_service.outbox.OutboxEventPublisher;
import com.saswat.razorpay.payment_service.repository.OrderRepository;
import com.saswat.razorpay.payment_service.repository.PaymentRepository;
import com.saswat.razorpay.payment_service.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentAuthorizationRecorder {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentTransitionService paymentTransitionService;
    private final OutboxEventPublisher eventPublisher;
    private final PaymentMapper paymentMapper;

    @Transactional
    public Payment recordPayment(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantIdForUpdate(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if (order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED) {
            throw  new BusinessRuleViolationException("ORDER_NOT_PAYABLE",
                    "Order cannot accept payment in status: "+order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .idempotencyKey(UUID.randomUUID().toString()) //Todo: idempotency
                .methodDetails(request.methodDetails())
                .build();
        payment = paymentRepository.save(payment);
        paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_ATTEMPT);
        return payment;

    }

    @Transactional
    public PaymentResponse compensateAuthorizationFailure(UUID paymentId, String errorCode,
                                                          String errorDescription) {
        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
        payment.setErrorCode(errorCode);
        payment.setErrorDescription(errorDescription);
        payment = paymentRepository.save(payment);

        publishStatusEvent(payment, "PAYMENT_AUTHORIZATION_COMPENSATED");
        return paymentMapper.toResponse(payment);

    }

    public PaymentResponse applyGatewayResult(UUID paymentId, PaymentResult result) {
        log.info("Applying Gateway result for paymentId: {}", paymentId);
        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure -> {
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }

            case PaymentResult.Success success ->
                log.warn("Invalid state: initiate() gateway call returned success directly, paymentId={}", paymentId);
        }

        payment = paymentRepository.save(payment);
        publishStatusEvent(payment, "PAYMENT_CREATED");
        log.info("Successfully applied gateway result for paymentId: {}", paymentId);
        return paymentMapper.toResponse(payment);
    }

    private void publishStatusEvent(Payment payment, String eventType) {
        eventPublisher.publish(EventAggregateType.PAYMENT, payment.getId(), eventType,
                Map.of("orderId", payment.getOrder().getId().toString(),
                        "paymentId", payment.getId().toString(),
                        "merchantId", payment.getMerchantId().toString(),
                        "paymentStatus", payment.getStatus().name(),
                        "amountUnits", payment.getAmount().getAmountUnits(),
                        "amountCurrency", payment.getAmount().getCurrency(),
                        "paymentMethod", payment.getMethod()
                ));

    }
}
