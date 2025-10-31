package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.AuthorRequestDto;
import com.learn_spring_boot.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    List<AuthorResponseDto> getAll();
    AuthorResponseDto getById(int id);
    AuthorResponseDto create(AuthorRequestDto dto);
    AuthorResponseDto update(int id, AuthorRequestDto dto);
    /**
     * Soft-delete the author and cascade soft-delete to related books.
     * @return number of related books that were soft-deleted
     */
    int delete(int id);
    void restore(int id);
    void forceDelete(int id);
}
