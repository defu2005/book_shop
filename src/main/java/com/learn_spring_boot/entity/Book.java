package com.learn_spring_boot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;


import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded  = true,callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Integer quantity;
    private Integer price;
    private Integer sold;
    @ManyToOne
    private Author author;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name="book_id"),inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> categories;
}
