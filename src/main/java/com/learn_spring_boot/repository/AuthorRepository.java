package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Author;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends SoftDeleteRepository<Author, Integer> {
}
