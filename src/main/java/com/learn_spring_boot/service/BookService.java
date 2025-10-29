package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.BookRequestDto;
import com.learn_spring_boot.dto.BookResponseDto;

import java.util.List;

public interface BookService {
    List<BookResponseDto> getAll();
    BookResponseDto getById(long id);
    BookResponseDto create(BookRequestDto dto);
    BookResponseDto update(long id, BookRequestDto dto);
    void delete(long id);
}
