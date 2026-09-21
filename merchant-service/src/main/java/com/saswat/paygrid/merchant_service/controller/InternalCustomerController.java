package com.saswat.paygrid.merchant_service.controller;

import com.saswat.paygrid.common_lib.dto.FindOrCreateCustomerRequest;
import com.saswat.paygrid.merchant_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/customers")
public class InternalCustomerController {

    private final CustomerService customerService;

    @PostMapping("/find-or-create")
    public UUID findOrCreate(@RequestBody FindOrCreateCustomerRequest request) {
        return customerService.findOrCreate(request.merchantId(), request.email(), request.name(), request.phone());
    }
}
