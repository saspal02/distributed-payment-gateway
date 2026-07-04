package com.saswat.razorpay.operations.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "settlement_payment")
public class SettlementPayment {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;
}
