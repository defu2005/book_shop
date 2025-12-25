package com.learn_spring_boot.enums;

public enum OrderStatus {
    PENDING,       // Order created but not yet processed
    CONFIRMED,     // Order confirmed by admin
    PROCESSING,    // Order is being prepared
    SHIPPING,      // Order is out for delivery
    DELIVERED,     // Order has been delivered successfully
    CANCELLED,     // Order was cancelled
    RETURNED,      // Order was returned by customer
    REFUNDED      // Order was refunded
}