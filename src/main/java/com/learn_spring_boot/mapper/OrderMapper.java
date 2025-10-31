package com.learn_spring_boot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.learn_spring_boot.entity.Order;
import com.learn_spring_boot.dto.OrderResponseDto;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "userId", expression = "java(order.getUser() != null ? order.getUser().getId() : null)")
    @Mapping(target = "createdAt", source = "createdAt")
    OrderResponseDto toDto(Order order);
}
