package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.BookRequestDto;
import com.learn_spring_boot.dto.BookResponseDto;
import com.learn_spring_boot.dto.BookSearchRequestDto;
import com.learn_spring_boot.entity.Author;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.entity.Category;
import com.learn_spring_boot.enums.BookSort;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.BookMapper;
import com.learn_spring_boot.repository.AuthorRepository;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.repository.CategoryRepository;
import com.learn_spring_boot.service.BookService;
import com.learn_spring_boot.specification.BookSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import com.learn_spring_boot.util.SecurityUtils;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final EntityManager em;
    @Override
    @Transactional
    public List<BookResponseDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookResponseDto getById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public void delete(long id) {
        Book existing = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        existing.setDeletedAt(Instant.now());
        existing.setDeletedBy(SecurityUtils.currentUsername());
        bookRepository.save(existing);
    }

    @Override
    @Transactional
    public void restore(long id) {
        Book existing = bookRepository.findByIdIncludingDeleted(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        existing.setDeletedAt(null);
        existing.setDeletedBy(null);
        bookRepository.save(existing);
    }

    @Override
    @Transactional
    public void forceDelete(long id) {
        Book existing = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id=" + id));
        bookRepository.delete(existing);
    }
    @Override
    public Page<BookResponseDto> getBooks(BookSearchRequestDto dto){
        int page = dto.page() != null ? dto.page() : 0;
        int size = dto.size() != null ? dto.size() : 20;
        Pageable pageable = PageRequest.of(page, size);
        Specification<Book> spec=Specification.<Book>unrestricted()
                .and(BookSpecification.hasNameOrAuthorNameContaining(dto.keyword()))
                .and(BookSpecification.hasAuthor(dto.authorId()))
                .and(BookSpecification.hasCategory(dto.categoryId()))
                .and(BookSpecification.hasPriceBetween(dto.minPrice(),dto.maxPrice()));
        Page<Book> books = bookRepository.findAll(spec, pageable);
        Page<BookResponseDto> res=books.map(book->bookMapper.toDto(book));
        return res;
    }
}
