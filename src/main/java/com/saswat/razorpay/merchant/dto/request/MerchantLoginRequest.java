package com.saswat.razorpay.merchant.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MerchantLoginRequest(

        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {


}
