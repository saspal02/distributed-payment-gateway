package com.saswat.razorpay.merchant.mapper;

import com.saswat.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant.dto.response.MerchantResponse;
import com.saswat.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest(MerchantSignUpRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
