package com.learn_spring_boot.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;

public record OrderRequestDto(
    @NotEmpty(message = "At least 1 book required!")
    Map<Long,Integer> books,
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address must not exceed 500 characters")
    String shippingAddress
) {}
