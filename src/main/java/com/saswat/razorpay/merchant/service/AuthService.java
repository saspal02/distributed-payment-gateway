package com.saswat.razorpay.merchant.service;

import com.saswat.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signUp(MerchantSignUpRequest request);
}
