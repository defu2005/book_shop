package com.learn_spring_boot.service.impl;

import com.learn_spring_boot.dto.AuthorRequestDto;
import com.learn_spring_boot.dto.AuthorResponseDto;
import com.learn_spring_boot.entity.Author;
import com.learn_spring_boot.exception.ResourceNotFoundException;
import com.learn_spring_boot.mapper.AuthorMapper;
import com.learn_spring_boot.repository.AuthorRepository;
import com.learn_spring_boot.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

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
    public AuthorResponseDto create(AuthorRequestDto dto) {
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        return authorMapper.toDto(saved);
    }

    @Override
    public AuthorResponseDto update(int id, AuthorRequestDto dto) {
    Author existing = authorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id=" + id));
        existing.setName(dto.name());
        Author saved = authorRepository.save(existing);
        return authorMapper.toDto(saved);
    }

    @Override
    public void delete(int id) {
        authorRepository.deleteById(id);
    }
}
