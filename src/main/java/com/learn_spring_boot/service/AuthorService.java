package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.AuthorRequestDto;
import com.learn_spring_boot.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    List<AuthorResponseDto> getAll();
    AuthorResponseDto getById(int id);
    AuthorResponseDto create(AuthorRequestDto dto);
    AuthorResponseDto update(int id, AuthorRequestDto dto);
    void delete(int id);
}
