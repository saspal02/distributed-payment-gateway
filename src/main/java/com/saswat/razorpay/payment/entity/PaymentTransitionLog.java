package com.saswat.razorpay.payment.entity;

import com.saswat.razorpay.common.entity.BaseEntity;
import com.saswat.razorpay.common.enums.PaymentEvent;
import com.saswat.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transition_log", indexes = {
        @Index(name = "idx_payment_transition_log_payment_id", columnList = "payment_id")
})
public class PaymentTransitionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 30)
    private PaymentEvent event;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private PaymentStatus toStatus;

    @Column(name = "actor", length = 100)
    private String actor;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
}
