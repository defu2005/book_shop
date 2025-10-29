package com.learn_spring_boot.dto;

/**
 * Simple DTO representing an author in responses.
 */
public record AuthorResponseDto(
        int id,
        String name
) {
}
