package com.learn_spring_boot.service;

import com.learn_spring_boot.entity.Order;
import com.learn_spring_boot.entity.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, List<Long> bookIds, List<Integer> quantities, String shippingAddress);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    List<Order> getAllOrders();
    Order updateOrderStatus(Long orderId, String status);
    void deleteOrder(Long id);
}
