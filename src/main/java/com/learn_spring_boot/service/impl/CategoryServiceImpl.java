package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.CategoryRequestDto;
import com.learn_spring_boot.dto.CategoryResponseDto;
import com.learn_spring_boot.entity.Category;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.CategoryMapper;
import com.learn_spring_boot.repository.CategoryRepository;
import com.learn_spring_boot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getById(int id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryResponseDto update(int id, CategoryRequestDto dto) {
    Category existing = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        existing.setName(dto.name());
        Category saved = categoryRepository.save(existing);
        return categoryMapper.toDto(saved);
    }

    @Override
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
