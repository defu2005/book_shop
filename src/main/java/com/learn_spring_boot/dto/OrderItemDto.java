package com.learn_spring_boot.dto;

public record OrderItemDto(
    Long id,
    Long bookId,
    String bookName,
    Integer quantity,
    Double price,
    Double subtotal
) {}
