package com.saswat.razorpay.vault_service.controller;

import com.saswat.razorpay.common_lib.dto.PaymentProcessorResponse;
import com.saswat.razorpay.common_lib.dto.VaultChargeRequest;
import com.saswat.razorpay.vault_service.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/vault")
public class InternalVaultController {

    private final VaultService vaultService;

    @PostMapping("/charge")
    public PaymentProcessorResponse charge(@RequestBody VaultChargeRequest request) {
        return vaultService.charge(request.paymentId(), request.token(), request.amount(), request.methodDetails());
    }
}
