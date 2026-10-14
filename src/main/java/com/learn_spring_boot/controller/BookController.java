package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.BookRequestDto;
import com.learn_spring_boot.dto.BookResponseDto;
import com.learn_spring_boot.dto.BookSearchRequestDto;
import com.learn_spring_boot.enums.BookSort;
import com.learn_spring_boot.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<?>> getBooks(@ModelAttribute BookSearchRequestDto requestDto) {
        Page<BookResponseDto> list = bookService.getBooks(requestDto);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Books fetched")
                .response(list)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getById(@PathVariable long id) {
        BookResponseDto dto = bookService.getById(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Book fetched")
                .response(dto)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> create(@RequestBody @Valid BookRequestDto request) {
        BookResponseDto dto = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.CREATED))
                .message("Book created")
                .response(dto)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> update(@PathVariable long id, @RequestBody @Valid BookRequestDto request) {
        BookResponseDto dto = bookService.update(id, request);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Book updated")
                .response(dto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> delete(@PathVariable long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Book deleted")
                .build());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ApiResponseDto<?>> restore(@PathVariable long id) {
        bookService.restore(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Book restored")
                .build());
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<ApiResponseDto<?>> forceDelete(@PathVariable long id) {
        bookService.forceDelete(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Book permanently deleted")
                .build());
    }

}
