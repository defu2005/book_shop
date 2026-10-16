package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.BookResponseDto;
import com.learn_spring_boot.dto.CartItemRequestDto;
import com.learn_spring_boot.dto.CartItemResponseDto;
import com.learn_spring_boot.dto.CartResponseDto;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.entity.Cart;
import com.learn_spring_boot.entity.CartItem;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.BookMapper;
import com.learn_spring_boot.mapper.CartItemMapper;
import com.learn_spring_boot.mapper.CartMapper;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.repository.CartItemRepository;
import com.learn_spring_boot.repository.CartRepository;
import com.learn_spring_boot.security.auth.UserDetailsImpl;
import com.learn_spring_boot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    public Cart getCartByCurrentUser(Authentication authentication){
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        Long userId=userDetails.getId();
        return cartRepository.findByUserId(userId);
    }
    @Override
    public CartResponseDto getCart(Authentication authentication){
        Cart cart= getCartByCurrentUser( authentication);
        long totalAmount=0;
        Set<CartItem> cartItems=cart.getCartItem();
        Set<CartItemResponseDto> cartItemResponseDtos= cartItems.stream().map(cartItem -> getCartItemResponseDto(cartItem)).collect(Collectors.toSet());
        totalAmount= cartItemResponseDtos.stream().mapToLong(CartItemResponseDto::subTotal).sum();
        return new CartResponseDto(cartItemResponseDtos,totalAmount);
    }
    @Override
    public CartItemResponseDto addCartItem(Authentication authentication,CartItemRequestDto cartItemRequestDto){

        Cart cart= getCartByCurrentUser( authentication);
        Book book=bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(()->new ResourceNotFoundException("Khong tim thay book id!"));
        CartItem cartItem=cartItemRepository.findByCartIdAndBookId(cart.getId(), book.getId());
        if(cartItem==null) cartItem=new CartItem();
        cartItem.setQuantity(cartItemRequestDto.quantity());
        cartItem.setBook(book);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
        return getCartItemResponseDto(cartItem);
    }
    @Override
    public CartItemResponseDto updateCartItem(Authentication authentication,Long cartItemId,CartItemRequestDto cartItemRequestDto){
        Cart cart= getCartByCurrentUser(authentication);
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(()->  new ResourceNotFoundException("Khong tim thay cart item!"));
        if(cart.getId()!=cartItem.getCart().getId())
        {
            throw new AccessDeniedException("Khong the thay doi gio hang cua user khac");
        }
        if (cartItemRequestDto.quantity() > cartItem.getBook().getQuantity()) {
            throw new IllegalArgumentException(
                    "Số lượng yêu cầu vượt quá số lượng tồn kho. Chỉ còn "
                            + cartItem.getBook().getQuantity() + " sản phẩm."
            );
        }
        cartItem.setQuantity(cartItemRequestDto.quantity());
        cartItemRepository.save(cartItem);
        return getCartItemResponseDto(cartItem);
    }
    @Override
    public void deleteCartItem(Authentication authentication ,Long cartItemId){
        Cart cart= getCartByCurrentUser(authentication);
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(()->  new ResourceNotFoundException("Khong tim thay cart item!"));
        if(cart.getId()!=cartItem.getCart().getId())
        {
            throw new AccessDeniedException("Khong the thay doi gio hang cua user khac");
        }
        cartItemRepository.delete(cartItem);
    }
    public CartItemResponseDto getCartItemResponseDto(CartItem cartItem){
        long subTotal=(cartItem.getBook().getPrice())*(cartItem.getQuantity());
        BookResponseDto bookResponseDto= bookMapper.toDto(cartItem.getBook());
        return new CartItemResponseDto(cartItem.getId(),bookResponseDto,cartItem.getQuantity(),subTotal);
    }
}
