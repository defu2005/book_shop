package com.learn_spring_boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO used when creating or updating a Book.
 * Fields map to `Book` entity: name, quantity, price, authorId and categoryIds.
 */
public record BookRequestDto(
        @NotBlank(message = "Book name is required!")
        @Size(max = 256, message = "Book name can have at most 256 characters!")
        String name,

        @PositiveOrZero(message = "Quantity must be greater than or equal to 0")
        Integer quantity,

        @PositiveOrZero(message = "Price must be greater than or equal to 0")
        Integer price,

        @NotNull(message = "Author id is required!")
        Integer authorId,

        /**
         * Optional set of category ids to associate with the book.
         */
        Set<Integer> categoryIds
) {
}
