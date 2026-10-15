package com.learn_spring_boot.mapper;

import com.learn_spring_boot.dto.CartResponseDto;
import com.learn_spring_boot.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    CartResponseDto toDto(Cart cart);
}
