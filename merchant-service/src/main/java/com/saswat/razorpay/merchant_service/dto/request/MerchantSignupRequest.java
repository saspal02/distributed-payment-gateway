package com.saswat.razorpay.merchant_service.dto.request;

import com.saswat.razorpay.common_lib.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(

        @NotNull(message = "Name is required")
        @Size(max = 50, message = "Name should not be more than 50 characters long")
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be least 8 characters long")
        String password,

        @Size(max = 50, message = "Business name should not be more that 50 characters long")
        String businessName,

        BusinessType businessType
) {

}
