package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.BookRequestDto;
import com.learn_spring_boot.dto.BookResponseDto;
import com.learn_spring_boot.dto.BookSearchRequestDto;
import com.learn_spring_boot.enums.BookSort;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface BookService {
    List<BookResponseDto> getAll();
    BookResponseDto getById(long id);
    BookResponseDto create(BookRequestDto dto);
    BookResponseDto update(long id, BookRequestDto dto);
    void delete(long id);
    void restore(long id);
    void forceDelete(long id);

    Page<BookResponseDto> getBooks(BookSearchRequestDto dto);
}
