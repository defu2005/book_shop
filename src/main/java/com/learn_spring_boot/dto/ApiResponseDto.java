package com.learn_spring_boot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record ApiResponseDto <T>(String status,String message,T response) {}
