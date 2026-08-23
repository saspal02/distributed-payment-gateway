package com.saswat.razorpay.merchant_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {


}
