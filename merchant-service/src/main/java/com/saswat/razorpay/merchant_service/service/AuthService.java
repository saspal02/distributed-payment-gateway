package com.saswat.razorpay.merchant_service.service;

import com.saswat.razorpay.merchant_service.dto.request.LoginRequest;
import com.saswat.razorpay.merchant_service.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant_service.dto.response.LoginResponse;
import com.saswat.razorpay.merchant_service.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signUp(MerchantSignUpRequest request);

    LoginResponse login(LoginRequest request);
}
