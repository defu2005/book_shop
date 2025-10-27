package com.learn_spring_boot.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record SignInResponseDto(String token, String type, Long id, String username, String email, List<String> roles) {
}
