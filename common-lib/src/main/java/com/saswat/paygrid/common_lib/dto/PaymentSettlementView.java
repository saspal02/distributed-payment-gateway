package com.saswat.paygrid.common_lib.dto;

import java.util.UUID;

public record PaymentSettlementView(
        UUID paymentId,
        int amountUnits,
        int refundedAmountUnits,
        String currency
) {
}
