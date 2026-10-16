package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.CartItemRequestDto;
import com.learn_spring_boot.dto.CartItemResponseDto;
import com.learn_spring_boot.dto.CartResponseDto;
import org.springframework.security.core.Authentication;

public interface CartService {
    CartResponseDto getCart(Authentication authentication);
    CartItemResponseDto addCartItem(Authentication authentication,CartItemRequestDto cartItemRequestDto);
    CartItemResponseDto updateCartItem(Authentication authentication,Long cartItemId,CartItemRequestDto cartItemRequestDto);
    void deleteCartItem(Authentication authentication,Long cartItemId);
}
