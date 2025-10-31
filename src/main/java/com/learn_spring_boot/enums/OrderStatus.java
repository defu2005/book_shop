package com.learn_spring_boot.enums;

public enum OrderStatus {
    PENDING,       // Order created but not yet processed
    PROCESSING,    // Order is being prepared(confirmed by admin)
    SHIPPING,      // Order is out for delivery
    DELIVERED,     // Order has been delivered successfully
    CANCELLED,     // Order was cancelled
    REFUNDED      // Order was refunded
}