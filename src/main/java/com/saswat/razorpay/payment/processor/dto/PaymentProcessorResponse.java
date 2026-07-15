package com.saswat.razorpay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Success,
        PaymentProcessorResponse.failure {

    record Pending(String processorRef) implements PaymentProcessorResponse {}

    record Success(String processorReference, String bankReference) implements PaymentProcessorResponse {}

    record failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
