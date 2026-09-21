package com.saswat.paygrid.payment_service.service.impl;

import com.saswat.paygrid.common_lib.enums.EventAggregateType;
import com.saswat.paygrid.common_lib.enums.OrderStatus;
import com.saswat.paygrid.payment_service.dto.request.CreateOrderRequest;
import com.saswat.paygrid.payment_service.dto.response.OrderResponse;
import com.saswat.paygrid.payment_service.entity.OrderRecord;
import com.saswat.paygrid.payment_service.mapper.OrderMapper;
import com.saswat.paygrid.payment_service.outbox.OutboxEventPublisher;
import com.saswat.paygrid.payment_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderPersistenceService {

    private final OrderRepository orderRepository;
    private final OutboxEventPublisher eventPublisher;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse persist(final UUID merchantId, final CreateOrderRequest request, final UUID customerId,
            final int defaultOrderExpiryMinutes) {
        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .customerId(customerId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        eventPublisher.publish(EventAggregateType.ORDER, order.getId(), "ORDER_CREATED",
                Map.of("orderId", order.getId(),
                        "merchantId", merchantId.toString(),
                        "orderStatus", order.getOrderStatus().name(),
                        "amountUnits", order.getAmount().getAmountUnits(),
                        "amountCurrency", order.getAmount().getCurrency()
                )
        );

        return orderMapper.toResponse(order);
    }
}
