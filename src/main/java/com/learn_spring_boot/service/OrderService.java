package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.OrderRequestDto;
import com.learn_spring_boot.entity.Order;
import com.learn_spring_boot.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {
    Order createOrder(Authentication authentication, OrderRequestDto orderRequestDto);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    List<Order> getOrdersForUser(Authentication authentication,User user);
    List<Order> getAllOrders();
    Order updateOrderStatus(Long orderId, String status);
    void deleteOrder(Long id);
}
