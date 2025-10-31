package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.AuthorRequestDto;
import com.learn_spring_boot.dto.AuthorResponseDto;
import com.learn_spring_boot.entity.Author;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.AuthorMapper;
import com.learn_spring_boot.repository.AuthorRepository;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.repository.BookRepository;
import com.learn_spring_boot.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.Instant;
import java.util.stream.Collectors;
import com.learn_spring_boot.util.SecurityUtils;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookRepository bookRepository;

    @Override
    public List<AuthorResponseDto> getAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDto getById(int id) {
    Author author = authorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    public AuthorResponseDto create(AuthorRequestDto dto) {
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        return authorMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AuthorResponseDto update(int id, AuthorRequestDto dto) {
    Author existing = authorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));
        existing.setName(dto.name());
        Author saved = authorRepository.save(existing);
        return authorMapper.toDto(saved);
    }

    @Override
    @Transactional
    public int delete(int id) {
        Author existing = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));

        // cascade soft-delete books by this author
        var books = bookRepository.findAllByAuthor_Id(existing.getId());
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
        authorRepository.save(existing);
        return count;
    }

    @Override
    @Transactional
    public void restore(int id) {
        Author existing = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));
        // restore related books
        var books = bookRepository.findAllByAuthor_Id(existing.getId());
        if (books != null && !books.isEmpty()) {
            for (Book b : books) {
                b.setDeletedAt(null);
                b.setDeletedBy(null);
            }
            bookRepository.saveAll(books);
        }
        existing.setDeletedAt(null);
        existing.setDeletedBy(null);
        authorRepository.save(existing);
    }

    @Override
    @Transactional
    public void forceDelete(int id) {
        Author existing = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));
        // hard-delete related books first to avoid FK constraint issues
        var books = bookRepository.findAllByAuthor_Id(existing.getId());
        if (books != null && !books.isEmpty()) {
            bookRepository.deleteAll(books);
        }
        authorRepository.delete(existing);
    }
}
