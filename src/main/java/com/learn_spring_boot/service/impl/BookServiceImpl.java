package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.BookRequestDto;
import com.learn_spring_boot.dto.BookResponseDto;
import com.learn_spring_boot.entity.Author;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.entity.Category;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.BookMapper;
import com.learn_spring_boot.repository.AuthorRepository;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.repository.CategoryRepository;
import com.learn_spring_boot.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookResponseDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto getById(long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto create(BookRequestDto dto) {
    Author author = authorRepository.findById(dto.authorId())
        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + dto.authorId()));

        Set<Category> categories = new HashSet<>();
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            categoryRepository.findAllById(dto.categoryIds()).forEach(categories::add);
        }

        Book book = new Book();
        book.setName(dto.name());
        book.setQuantity(dto.quantity());
        book.setPrice(dto.price());
        book.setAuthor(author);
        book.setCategories(categories);

        Book saved = bookRepository.save(book);
        return bookMapper.toDto(saved);
    }

    @Override
    public BookResponseDto update(long id, BookRequestDto dto) {
    Book existing = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        
        if (dto.name() != null) existing.setName(dto.name());
        if (dto.quantity() != null) existing.setQuantity(dto.quantity());
        if (dto.price() != null) existing.setPrice(dto.price());

        if (dto.authorId() != null) {
        Author author = authorRepository.findById(dto.authorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + dto.authorId()));
            existing.setAuthor(author);
        }

        if (dto.categoryIds() != null) {
            Set<Category> categories = new HashSet<>();
            categoryRepository.findAllById(dto.categoryIds()).forEach(categories::add);
            existing.setCategories(categories);
        }

        Book saved = bookRepository.save(existing);
        return bookMapper.toDto(saved);
    }

    @Override
    public void delete(long id) {
        bookRepository.deleteById(id);
    }
}
