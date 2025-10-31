package com.learn_spring_boot.service;

import com.learn_spring_boot.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}
