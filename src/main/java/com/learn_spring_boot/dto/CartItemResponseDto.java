package com.learn_spring_boot.dto;

public record CartItemResponseDto(
        Long cartItemId,
        BookResponseDto book,
        Integer quantity,
        Long subTotal
) {
}
