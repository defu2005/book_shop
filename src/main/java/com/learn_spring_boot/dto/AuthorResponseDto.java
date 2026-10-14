package com.learn_spring_boot.dto;

/**
 * Simple DTO representing an author in responses.
 */
public record AuthorResponseDto(
        Integer id,
        String name
) {
}
