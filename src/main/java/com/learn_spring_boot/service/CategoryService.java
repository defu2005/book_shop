package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.CategoryRequestDto;
import com.learn_spring_boot.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> getAll();
    CategoryResponseDto getById(int id);
    CategoryResponseDto create(CategoryRequestDto dto);
    CategoryResponseDto update(int id, CategoryRequestDto dto);
    void delete(int id);
}
