package com.learn_spring_boot.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    private int id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Book> books;
}
