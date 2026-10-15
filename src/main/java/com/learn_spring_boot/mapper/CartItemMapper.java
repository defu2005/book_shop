package com.learn_spring_boot.mapper;

import com.learn_spring_boot.dto.CartItemRequestDto;
import com.learn_spring_boot.dto.CartItemResponseDto;
import com.learn_spring_boot.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);
    CartItemResponseDto toDto(CartItem cartItem);
}
