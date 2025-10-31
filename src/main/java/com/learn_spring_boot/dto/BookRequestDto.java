package com.learn_spring_boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BookRequestDto(
        @NotBlank(message = "Book name is required")
        @Size(max = 256, message = "Book name must not exceed 256 characters")
        String name,

        @NotNull(message = "Quantity is required")
        @PositiveOrZero(message = "Quantity must be greater than or equal to 0")
        Integer quantity,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be greater than or equal to 0")
        Integer price,

        @NotNull(message = "Author ID is required")
        Integer authorId,

        @NotNull(message = "Category IDs are required")
        @NotEmpty(message = "At least one category must be selected")
        Set<Integer> categoryIds
) {
}
