package com.learn_spring_boot.dto;

/**
 * Simple DTO representing a category in responses.
 */
public record CategoryResponseDto(
        int id,
        String name
) {
}
