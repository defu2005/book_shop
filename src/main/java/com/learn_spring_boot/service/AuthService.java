package com.learn_spring_boot.service;

import com.learn_spring_boot.dto.ApiResponseDto;
import com.learn_spring_boot.dto.SignInRequestDto;
import com.learn_spring_boot.dto.SignUpRequestDto;
import com.learn_spring_boot.exception.RoleNotFoundException;
import com.learn_spring_boot.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> signUp(SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException, RoleNotFoundException;
    ResponseEntity<ApiResponseDto<?>> signIn(SignInRequestDto signInRequestDto);
}

