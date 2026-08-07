package com.saswat.razorpay.common.exception;

import lombok.Getter;

@Getter
public class IdempotencyConflictException extends RuntimeException {

    private final String errorCode;

    public IdempotencyConflictException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;

    }
}
