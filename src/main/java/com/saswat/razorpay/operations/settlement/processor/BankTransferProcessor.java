package com.saswat.razorpay.operations.settlement.processor;

import com.saswat.razorpay.common.entity.Money;
import com.saswat.razorpay.operations.settlement.dto.BankTransferResult;

import java.util.UUID;

public interface BankTransferProcessor {

    BankTransferResult initiate(UUID settlementId, UUID merchantId, Money amount,
                                String bankAccount, String ifsc);
}
