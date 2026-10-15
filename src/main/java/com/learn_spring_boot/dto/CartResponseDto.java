package com.learn_spring_boot.dto;

import java.util.Set;

public record CartResponseDto(
        Set<CartItemResponseDto> cartItems,
        Long totalAmount
) {
}
