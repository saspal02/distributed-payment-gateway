package com.saswat.razorpay.payment_service.mapper;

import com.saswat.razorpay.payment_service.dto.response.OrderResponse;
import com.saswat.razorpay.payment_service.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
