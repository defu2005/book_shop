package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.AuthorRequestDto;
import com.learn_spring_boot.dto.AuthorResponseDto;
import com.learn_spring_boot.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<?>> getAll() {
        List<AuthorResponseDto> list = authorService.getAll();
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Authors fetched")
                .response(list)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getById(@PathVariable int id) {
        AuthorResponseDto dto = authorService.getById(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Author fetched")
                .response(dto)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> create(@RequestBody @Valid AuthorRequestDto request) {
        AuthorResponseDto dto = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.CREATED))
                .message("Author created")
                .response(dto)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> update(@PathVariable int id, @RequestBody @Valid AuthorRequestDto request) {
        AuthorResponseDto dto = authorService.update(id, request);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Author updated")
                .response(dto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> delete(@PathVariable int id) {
        int cascaded = authorService.delete(id);
        String message = "Author deleted";
        if (cascaded > 0) {
            message += ". Warning: " + cascaded + " related books were also soft-deleted.";
        }
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message(message)
                .build());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ApiResponseDto<?>> restore(@PathVariable int id) {
        authorService.restore(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Author restored")
                .build());
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<ApiResponseDto<?>> forceDelete(@PathVariable int id) {
        authorService.forceDelete(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Author permanently deleted")
                .build());
    }
}
