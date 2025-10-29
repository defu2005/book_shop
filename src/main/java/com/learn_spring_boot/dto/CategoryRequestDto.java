package com.learn_spring_boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used when creating or updating a Category.
 */
public record CategoryRequestDto(
        @NotBlank(message = "Category name is required!")
        @Size(max = 256, message = "Category name can have at most 256 characters!")
        String name
) {
}
