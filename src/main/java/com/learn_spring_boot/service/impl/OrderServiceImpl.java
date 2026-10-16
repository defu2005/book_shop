package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.OrderRequestDto;
import com.learn_spring_boot.entity.*;
import com.learn_spring_boot.enums.OrderStatus;
import com.learn_spring_boot.repository.*;
import com.learn_spring_boot.security.auth.UserDetailsImpl;
import com.learn_spring_boot.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    @Override
    @Transactional
    public Order createOrder(Authentication authentication, OrderRequestDto orderRequestDto ) {
        Order order = new Order();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        User user=userRepository.findById(userDetails.getId()).orElseThrow(()->new UsernameNotFoundException("User not found!"));
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setOrderItems(new ArrayList<>());
        Cart cart=cartRepository.findByUserId(user.getId());
        orderRequestDto.books().forEach((bookId,quantity)-> {
            Book book = bookRepository.findById(bookId).orElseThrow();
            if(book.getQuantity()<quantity)
            {
                throw new AccessDeniedException("Khong du hang trong kho!");
            }
            double total = 0.0;
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(quantity);

            CartItem cartItem=cartItemRepository.findByCartIdAndBookId(cart.getId(),bookId);
            cartItemRepository.delete(cartItem);
            double price = book.getPrice() != null ? book.getPrice().doubleValue() : 0.0;
            item.setPrice(price);
            item.setSubtotal(price * quantity);
            order.getOrderItems().add(item);
            total += item.getSubtotal();
            order.setTotalAmount(total);
        });
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
    public List<Order> getOrdersForUser( Authentication authentication,User user) {
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        User checkUser=userRepository.findById(userDetails.getId()).orElseThrow(()->new UsernameNotFoundException("User not found!"));
        if(checkUser.getId()!=user.getId()){
            throw new AccessDeniedException("Khong the lay orders cua user khac!");
        }
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
