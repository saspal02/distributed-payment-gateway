package com.saswat.razorpay.common.money;

import jakarta.persistence.Embeddable;

@Embeddable
public class Money {

    private int amountUnits;
    private String currency;

    private Money(int amountUnits, String currency) {
        this.amountUnits = amountUnits;
        this.currency = currency;
    }

    public static Money of(int amountUnits, String currency) {
        return new Money(amountUnits, currency);
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
