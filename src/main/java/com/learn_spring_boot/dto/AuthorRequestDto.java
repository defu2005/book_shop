package com.learn_spring_boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used when creating or updating an Author.
 */
public record AuthorRequestDto(
        @NotBlank(message = "Author name is required!")
        @Size(max = 256, message = "Author name can have at most 256 characters!")
        String name
) {
}
