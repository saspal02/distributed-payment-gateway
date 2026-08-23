package com.saswat.razorpay.operations_service.settlement.processor;

import com.saswat.razorpay.common_lib.entity.Money;
import com.saswat.razorpay.operations_service.settlement.dto.BankTransferResult;

import java.util.UUID;

public interface BankTransferProcessor {

    BankTransferResult initiate(UUID settlementId, UUID merchantId, Money amount,
                                String bankAccount, String ifsc);
}
