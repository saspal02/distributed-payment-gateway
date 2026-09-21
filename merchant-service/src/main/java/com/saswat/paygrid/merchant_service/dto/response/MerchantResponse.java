package com.saswat.paygrid.merchant_service.dto.response;

import com.saswat.paygrid.common_lib.enums.BusinessType;
import com.saswat.paygrid.common_lib.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus

) {
}
