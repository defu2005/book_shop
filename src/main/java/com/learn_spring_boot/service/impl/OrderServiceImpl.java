package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.entity.*;
import com.learn_spring_boot.enums.OrderStatus;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.repository.OrderItemRepository;
import com.learn_spring_boot.repository.OrderRepository;
import com.learn_spring_boot.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Order createOrder(User user, List<Long> bookIds, List<Integer> quantities, String shippingAddress) {
        if (bookIds.size() != quantities.size()) throw new IllegalArgumentException("BookIds and quantities size mismatch");
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setOrderItems(new ArrayList<>());
        double total = 0.0;
        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepository.findById(bookIds.get(i)).orElseThrow();
            int qty = quantities.get(i);
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(qty);
            double price = book.getPrice() != null ? book.getPrice().doubleValue() : 0.0;
            item.setPrice(price);
            item.setSubtotal(price * qty);
            order.getOrderItems().add(item);
            total += item.getSubtotal();
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAll().stream().filter(o -> o.getUser().getId().equals(user.getId())).toList();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
