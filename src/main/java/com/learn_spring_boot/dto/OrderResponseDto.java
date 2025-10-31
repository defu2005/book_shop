package com.learn_spring_boot.dto;

import com.learn_spring_boot.enums.OrderStatus;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
    Long id,
    Long userId,
    OrderStatus status,
    String shippingAddress,
    Double totalAmount,
    List<OrderItemDto> items,
    Instant createdAt
) {}
