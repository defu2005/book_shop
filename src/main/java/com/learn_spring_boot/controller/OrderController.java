package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.*;
import com.learn_spring_boot.entity.Order;
import com.learn_spring_boot.entity.User;
import com.learn_spring_boot.mapper.OrderMapper;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.repository.UserRepository;
import com.learn_spring_boot.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> createOrder(@RequestBody @Valid OrderRequestDto req){
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.userId()));
        Order order = orderService.createOrder(user, req.bookIds(), req.quantities(), req.shippingAddress());
    return ResponseEntity.ok(ApiResponseDto.<OrderResponseDto>builder()
        .status("success")
        .message("Order created")
        .response(orderMapper.toDto(order))
        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> getOrder(@PathVariable Long id){
        Order order = orderService.getOrderById(id);
    return ResponseEntity.ok(ApiResponseDto.<OrderResponseDto>builder()
        .status("success")
        .message("Order fetched")
        .response(orderMapper.toDto(order))
        .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getOrdersByUser(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderService.getOrdersByUser(user);
    List<OrderResponseDto> dtos = orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDto.<List<OrderResponseDto>>builder()
                .status("success")
                .message("Orders fetched")
                .response(dtos)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getAll(){
        List<Order> orders = orderService.getAllOrders();
    List<OrderResponseDto> dtos = orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDto.<List<OrderResponseDto>>builder()
                .status("success")
                .message("All orders")
                .response(dtos)
                .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> updateStatus(@PathVariable Long id, @RequestBody String status){
        Order updated = orderService.updateOrderStatus(id, status.replace("\"",""));
    return ResponseEntity.ok(ApiResponseDto.<OrderResponseDto>builder()
        .status("success")
        .message("Status updated")
        .response(orderMapper.toDto(updated))
        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> delete(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponseDto.builder().status("success").message("Order deleted").response(null).build());
    }
}
