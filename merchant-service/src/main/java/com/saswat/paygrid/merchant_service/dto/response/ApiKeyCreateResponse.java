package com.saswat.paygrid.merchant_service.dto.response;

import com.saswat.paygrid.common_lib.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {

}