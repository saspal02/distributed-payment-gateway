package com.saswat.razorpay.merchant.mapper;

import com.saswat.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.saswat.razorpay.merchant.dto.response.ApiKeyResponse;
import com.saswat.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyCreateResponse toCreateResponse(ApiKey apiKey);

    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
