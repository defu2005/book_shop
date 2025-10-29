package com.learn_spring_boot.dto;

import java.util.Set;

/**
 * DTO returned to clients for Book data.
 */
public record BookResponseDto(
        long id,
        String name,
        Integer quantity,
        Integer price,
        AuthorResponseDto author,
        Set<CategoryResponseDto> categories
) {
}
