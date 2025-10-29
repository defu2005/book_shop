package com.learn_spring_boot.mapper;

import org.mapstruct.Mapper;
import com.learn_spring_boot.entity.Author;
import com.learn_spring_boot.dto.AuthorResponseDto;
import com.learn_spring_boot.dto.AuthorRequestDto;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorResponseDto toDto(Author author);
    Author toEntity(AuthorRequestDto dto);
}
