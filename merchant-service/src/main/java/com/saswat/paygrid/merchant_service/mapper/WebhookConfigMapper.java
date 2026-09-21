package com.saswat.paygrid.merchant_service.mapper;

import com.saswat.paygrid.merchant_service.dto.response.WebhookConfigResponse;
import com.saswat.paygrid.merchant_service.entity.MerchantWebhookConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebhookConfigMapper {

    @Mapping(target = "webhookSecret", source = "rawSecret")
    WebhookConfigResponse toResponse(MerchantWebhookConfig merchantWebHookConfig, String rawSecret);
}
