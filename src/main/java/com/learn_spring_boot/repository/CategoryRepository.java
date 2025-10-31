package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends SoftDeleteRepository<Category, Integer> {
}
