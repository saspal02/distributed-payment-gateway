package com.saswat.paygrid.vault_service.dto.response;

import com.saswat.paygrid.common_lib.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
