package com.saswat.razorpay.payment.service.impl;

import com.saswat.razorpay.common.enums.OrderStatus;
import com.saswat.razorpay.common.exception.BusinessRuleViolationException;
import com.saswat.razorpay.common.exception.DuplicateResourceException;
import com.saswat.razorpay.common.exception.ResourceNotFoundException;
import com.saswat.razorpay.payment.dto.request.CreateOrderRequest;
import com.saswat.razorpay.payment.dto.response.OrderResponse;
import com.saswat.razorpay.payment.dto.response.PaymentResponse;
import com.saswat.razorpay.payment.entity.OrderRecord;
import com.saswat.razorpay.payment.entity.Payment;
import com.saswat.razorpay.payment.mapper.OrderMapper;
import com.saswat.razorpay.payment.mapper.PaymentMapper;
import com.saswat.razorpay.payment.repository.OrderRepository;
import com.saswat.razorpay.payment.repository.PaymentRepository;
import com.saswat.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

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

        return orderMapper.toResponse(order);

    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (order.getOrderStatus() == (OrderStatus.CANCELLED)
                || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Cannot cancel order with status " + order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(order);

        return paymentMapper.toResponseList(paymentList);
    }
}
