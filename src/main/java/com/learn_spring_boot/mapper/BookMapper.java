package com.learn_spring_boot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.dto.BookResponseDto;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, CategoryMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    BookResponseDto toDto(Book book);
}
