package com.saswat.razorpay.common_lib.ratelimit;

public record RateLimitResult(boolean isAllowed, int remaining, int retryAfterSeconds) {

    public static RateLimitResult allowed(int remaining) {
        return new RateLimitResult(true, remaining, 0);
    }

    public static RateLimitResult denied(int retryAfterSeconds) {
        return new RateLimitResult(false, -1, retryAfterSeconds);
    }
}
