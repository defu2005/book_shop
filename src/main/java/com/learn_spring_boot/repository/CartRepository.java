package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(long userId);
}
