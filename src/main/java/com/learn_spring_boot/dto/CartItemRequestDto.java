package com.learn_spring_boot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @NotNull(message = "Book ID is required!")
        Long bookId,
        @Positive(message = "Quantity must greater than 0")
        int quantity
) {
}
