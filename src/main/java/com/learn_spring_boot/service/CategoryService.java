package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.CategoryRequestDto;
import com.learn_spring_boot.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> getAll();
    CategoryResponseDto getById(int id);
    CategoryResponseDto create(CategoryRequestDto dto);
    CategoryResponseDto update(int id, CategoryRequestDto dto);
    /**
     * Soft-delete the category and cascade soft-delete to related books.
     * @return number of related books that were soft-deleted
     */
    int delete(int id);
    void restore(int id);
    void forceDelete(int id);
}
