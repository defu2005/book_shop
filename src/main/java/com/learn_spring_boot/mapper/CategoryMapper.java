package com.learn_spring_boot.mapper;

import org.mapstruct.Mapper;
import com.learn_spring_boot.entity.Category;
import com.learn_spring_boot.dto.CategoryResponseDto;
import com.learn_spring_boot.dto.CategoryRequestDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);
    Category toEntity(CategoryRequestDto dto);
}
