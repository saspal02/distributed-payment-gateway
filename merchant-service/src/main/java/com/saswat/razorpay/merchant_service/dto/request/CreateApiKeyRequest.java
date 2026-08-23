package com.saswat.razorpay.merchant_service.dto.request;

import com.saswat.razorpay.common_lib.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}