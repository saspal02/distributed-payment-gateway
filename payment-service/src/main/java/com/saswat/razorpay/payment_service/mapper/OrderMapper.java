package com.saswat.razorpay.payment_service.mapper;

import com.saswat.razorpay.payment_service.dto.response.OrderResponse;
import com.saswat.razorpay.payment_service.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord orderRecord);

    List<OrderResponse> toResponseList(List<OrderRecord> orderRecords);
}
