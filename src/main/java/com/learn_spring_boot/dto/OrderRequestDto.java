package com.learn_spring_boot.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequestDto(
    @NotNull(message = "User ID is required")
    Long userId,

    @NotNull(message = "Book IDs list is required")
    @Size(min = 1, message = "Order must contain at least one book")
    List<Long> bookIds,

    @NotNull(message = "Quantities list is required")
    @Size(min = 1, message = "Order must contain at least one quantity")
    List<Integer> quantities,

    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address must not exceed 500 characters")
    String shippingAddress
) {}
