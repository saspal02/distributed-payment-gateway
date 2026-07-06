package com.saswat.razorpay.payment.service.impl;

import com.saswat.razorpay.common.enums.OrderStatus;
import com.saswat.razorpay.common.exception.DuplicateResourceException;
import com.saswat.razorpay.payment.dto.request.CreateOrderRequest;
import com.saswat.razorpay.payment.dto.response.OrderResponse;
import com.saswat.razorpay.payment.entity.OrderRecord;
import com.saswat.razorpay.payment.repository.OrderRepository;
import com.saswat.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Value("${payment.order.Default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())) {
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: "
                    + request);
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        // TODO: publish kafka event about order creation

        return new OrderResponse(order.getId(), order.getMerchantId(), order.getReceipt(), order.getAmount(),
                order.getOrderStatus(), order.getAttempts(), order.getNotes()
                ,order.getExpiresAt(), null);

    }
}
