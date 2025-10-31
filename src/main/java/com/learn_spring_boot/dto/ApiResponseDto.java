package com.learn_spring_boot.dto;

import lombok.Builder;
@Builder
public record ApiResponseDto <T>(String status,String message,T response) {}
