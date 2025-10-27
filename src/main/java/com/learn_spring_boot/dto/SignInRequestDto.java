package com.learn_spring_boot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequestDto(
        @NotBlank(message = "Email is required!")
        @Email(message = "Email is not in valid format!")
        String email,
        @NotBlank(message = "Password is required!")
        @Size(min = 6,message = "Password must have at least 6 characters!")
        @Size(max = 20,message = "Password can have at most 20 characters!")
        String password) {}
