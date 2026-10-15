package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartIdAndBookId(Long cartId,Long bookId);
}
