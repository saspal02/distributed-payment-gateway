package com.saswat.razorpay.common.exception;

public class IdempotencyConflictException extends RuntimeException {
    public IdempotencyConflictException(String message) {
        super(message);
    }
}
