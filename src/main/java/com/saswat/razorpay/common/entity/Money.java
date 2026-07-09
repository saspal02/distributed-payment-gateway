package com.saswat.razorpay.common.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Money {

    private int amountUnits;
    private String currency;

    public static Money of(int amountUnits, String currency) {
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits) {
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other) {
        if(!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with existing currencies");
        }

        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other) {
        if(!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with existing currencies");
        }

        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }

}
