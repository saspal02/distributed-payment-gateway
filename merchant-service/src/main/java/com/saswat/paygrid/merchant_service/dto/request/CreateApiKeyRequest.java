package com.saswat.paygrid.merchant_service.dto.request;

import com.saswat.paygrid.common_lib.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}