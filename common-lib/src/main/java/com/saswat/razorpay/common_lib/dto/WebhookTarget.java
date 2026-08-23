package com.saswat.razorpay.common_lib.dto;

import java.util.UUID;

public record WebhookTarget(UUID configId, String targetUrl, String webhookSecret) {
}
