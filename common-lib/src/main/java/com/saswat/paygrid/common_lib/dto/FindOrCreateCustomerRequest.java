package com.saswat.paygrid.common_lib.dto;

import java.util.UUID;

public record FindOrCreateCustomerRequest(
        UUID merchantId,
        String email,
        String name,
        String phone
) {
}
