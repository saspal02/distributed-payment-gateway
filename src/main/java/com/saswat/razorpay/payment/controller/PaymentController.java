package com.saswat.razorpay.payment.controller;

import com.saswat.razorpay.payment.dto.request.PaymentInitRequest;
import com.saswat.razorpay.payment.dto.response.PaymentResponse;
import com.saswat.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("546c96cd-b195-46a3-9083-4ba1e3741048"); //TODO: replace it with merchant

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@RequestBody PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId, request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.capture(merchantId, paymentId));
    }
}
