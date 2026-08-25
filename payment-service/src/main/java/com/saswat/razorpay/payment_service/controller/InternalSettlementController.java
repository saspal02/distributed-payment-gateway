package com.saswat.razorpay.payment_service.controller;

import com.saswat.razorpay.common_lib.dto.PaymentSettlementView;
import com.saswat.razorpay.payment_service.api.PaymentLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/payments")
public class InternalSettlementController {

    private final PaymentLookupService settlementLookupService;

    @GetMapping("/unsettled-captured")
    public List<PaymentSettlementView> findUnsettledCaptured(@RequestParam UUID merchantId) {
        return settlementLookupService.findUnsettledCapturedPayments(merchantId);
    }

    @PostMapping("/mark-settled")
    public void markSettled(@RequestBody List<UUID> paymentIds) {
        settlementLookupService.markSettled(paymentIds);
    }
}
