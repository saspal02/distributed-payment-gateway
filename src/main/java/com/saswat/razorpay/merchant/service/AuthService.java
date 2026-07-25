package com.saswat.razorpay.merchant.service;

import com.saswat.razorpay.merchant.dto.request.LoginRequest;
import com.saswat.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant.dto.response.LoginResponse;
import com.saswat.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signUp(MerchantSignUpRequest request);

    LoginResponse login(LoginRequest request);
}
