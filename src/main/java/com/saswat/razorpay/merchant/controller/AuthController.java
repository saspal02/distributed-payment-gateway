package com.saswat.razorpay.merchant.controller;

import com.saswat.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant.dto.response.MerchantResponse;
import com.saswat.razorpay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse> signUp(@RequestBody @Valid MerchantSignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.signUp(request)
        );

    }

}
