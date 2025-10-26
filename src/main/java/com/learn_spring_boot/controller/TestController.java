package com.learn_spring_boot.controller;

import com.learn_spring_boot.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/test")
public class TestController {
    @GetMapping
    public ResponseEntity<ApiResponseDto<?>> Test(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.builder()
                .status(String.valueOf(HttpStatus.OK))
                .message("Securing Spring Boot using Spring Security and JWT")
                .build());
    }
}
