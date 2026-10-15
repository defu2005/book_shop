package com.learn_spring_boot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded  = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart",orphanRemoval = true,cascade = CascadeType.PERSIST)
    private Set<CartItem> cartItem;
    @OneToOne(mappedBy = "cart")
    private User user;
}
