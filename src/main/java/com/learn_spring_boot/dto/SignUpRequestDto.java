package com.learn_spring_boot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SignUpRequestDto(
        @NotBlank(message = "Username is required!")
        @Size(min = 6,message = "Username must have at least 6 characters!")
        @Size(max=20,message = "Username can have at most 20 characters!")
        String username,
        @Email(message = "Email is not in valid format!")
        @NotBlank(message = "Email is required!")
        String email,
        @NotBlank(message = "Password is required!")
        @Size(min = 6,message = "Password must have at least 6 characters!")
        @Size(max = 20,message = "Password can have at most 20 characters!")
        String password,
        Set<String> roles
) {
}
