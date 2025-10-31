package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.CategoryRequestDto;
import com.learn_spring_boot.dto.CategoryResponseDto;
import com.learn_spring_boot.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<?>> getAll() {
        List<CategoryResponseDto> list = categoryService.getAll();
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Categories fetched")
                .response(list)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getById(@PathVariable int id) {
        CategoryResponseDto dto = categoryService.getById(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Category fetched")
                .response(dto)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<?>> create(@RequestBody @Valid CategoryRequestDto request) {
        CategoryResponseDto dto = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.CREATED))
                .message("Category created")
                .response(dto)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> update(@PathVariable int id, @RequestBody @Valid CategoryRequestDto request) {
        CategoryResponseDto dto = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Category updated")
                .response(dto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> delete(@PathVariable int id) {
        int cascaded = categoryService.delete(id);
        String message = "Category deleted";
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
        categoryService.restore(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Category restored")
                .build());
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<ApiResponseDto<?>> forceDelete(@PathVariable int id) {
        categoryService.forceDelete(id);
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Category permanently deleted")
                .build());
    }
}
