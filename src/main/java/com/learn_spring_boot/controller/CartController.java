package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.CartItemRequestDto;
import com.learn_spring_boot.dto.CartItemResponseDto;
import com.learn_spring_boot.dto.CartResponseDto;
import com.learn_spring_boot.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {
    private final CartService cartService;
    @GetMapping
    public ResponseEntity<ApiResponseDto> getCart(Authentication authentication){
        CartResponseDto cart=cartService.getCart(authentication);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Cart fetched!")
                .response(cart)
                .build());
    }
    @PostMapping
    public ResponseEntity<ApiResponseDto> addCartItem(@RequestBody @Valid CartItemRequestDto dto,Authentication authentication){
        CartItemResponseDto res= cartService.addCartItem(authentication, dto);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Cart Item added!")
                .response(res)
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateCartItem(@PathVariable Long id,@RequestBody@Valid CartItemRequestDto dto){
        CartItemResponseDto res=cartService.updateCartItem(id, dto);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Cart Item updated!")
                .response(res)
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteCartItem(@PathVariable Long id){
        cartService.deleteCartItem(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Cart Item deleted!")
                .build());
    }
}
