package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.CategoryRequestDto;
import com.learn_spring_boot.dto.CategoryResponseDto;
import com.learn_spring_boot.entity.Category;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.mapper.CategoryMapper;
import com.learn_spring_boot.repository.CategoryRepository;
import com.learn_spring_boot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.Instant;
import java.util.stream.Collectors;
import com.learn_spring_boot.util.SecurityUtils;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;

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
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CategoryResponseDto update(int id, CategoryRequestDto dto) {
    Category existing = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        existing.setName(dto.name());
        Category saved = categoryRepository.save(existing);
        return categoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public int delete(int id) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        // cascade soft-delete books belonging to this category
        var books = bookRepository.findAllByCategories_Id(existing.getId());
        int count = 0;
        if (books != null && !books.isEmpty()) {
            Instant now = Instant.now();
            String user = SecurityUtils.currentUsername();
            for (Book b : books) {
                if (b.getDeletedAt() == null) {
                    b.setDeletedAt(now);
                    b.setDeletedBy(user);
                    count++;
                }
            }
            bookRepository.saveAll(books);
        }
        existing.setDeletedAt(Instant.now());
        existing.setDeletedBy(SecurityUtils.currentUsername());
        categoryRepository.save(existing);
        return count;
    }

    @Override
    @Transactional
    public void restore(int id) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        var books = bookRepository.findAllByCategories_Id(existing.getId());
        if (books != null && !books.isEmpty()) {
            for (Book b : books) {
                b.setDeletedAt(null);
                b.setDeletedBy(null);
            }
            bookRepository.saveAll(books);
        }
        existing.setDeletedAt(null);
        existing.setDeletedBy(null);
        categoryRepository.save(existing);
    }

    @Override
    @Transactional
    public void forceDelete(int id) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id=" + id));
        // hard-delete books first to avoid FK constraint issues
        var books = bookRepository.findAllByCategories_Id(existing.getId());
        if (books != null && !books.isEmpty()) {
            bookRepository.deleteAll(books);
        }
        categoryRepository.delete(existing);
    }
}
