package com.saswat.razorpay.payment.service;

import com.saswat.razorpay.payment.dto.request.CreateOrderRequest;
import com.saswat.razorpay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse create(UUID merchantId, CreateOrderRequest request);
}
