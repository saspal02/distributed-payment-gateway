package com.saswat.razorpay.payment.mapper;

import com.saswat.razorpay.payment.dto.response.OrderResponse;
import com.saswat.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);
}
