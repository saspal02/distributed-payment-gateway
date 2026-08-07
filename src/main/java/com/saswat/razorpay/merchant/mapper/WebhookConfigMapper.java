package com.saswat.razorpay.merchant.mapper;

import com.saswat.razorpay.merchant.dto.response.WebhookConfigResponse;
import com.saswat.razorpay.merchant.entity.MerchantWebHookConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebhookConfigMapper {

    @Mapping(target = "webhookSecret", source = "rawSecret")
    WebhookConfigResponse toResponse(MerchantWebHookConfig merchantWebHookConfig, String rawSecret);
}
