package com.saswat.paygrid.merchant_service.service;

import com.saswat.paygrid.merchant_service.dto.request.UpdateWebhookConfigRequest;
import com.saswat.paygrid.merchant_service.dto.response.WebhookConfigResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface WebhookConfigService {

    WebhookConfigResponse create(UUID merchantId, UpdateWebhookConfigRequest request);

    List<WebhookConfigResponse> list(UUID merchantId);

    WebhookConfigResponse getById(UUID merchantId, UUID id);

    WebhookConfigResponse update(UUID merchantId, UUID id, @Valid UpdateWebhookConfigRequest request);

    void delete(UUID merchantId, UUID id);
}
