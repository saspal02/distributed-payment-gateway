package com.saswat.razorpay.merchant.dto.request;

import com.saswat.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}