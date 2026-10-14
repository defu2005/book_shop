package com.learn_spring_boot.dto;
import com.learn_spring_boot.enums.BookSort;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Set;

public record BookSearchRequestDto(
        @Size(max = 256, message = "keyword must not exceed 256 characters")
        String keyword,
        Integer authorId,
        Integer categoryId,
        Integer maxPrice,
        Integer minPrice,
        BookSort sort,
        Integer page,
        Integer size

) {
}