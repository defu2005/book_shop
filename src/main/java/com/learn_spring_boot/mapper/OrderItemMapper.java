package com.learn_spring_boot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.learn_spring_boot.entity.OrderItem;
import com.learn_spring_boot.dto.OrderItemDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(target = "bookId", expression = "java(item.getBook() != null ? item.getBook().getId() : null)")
    @Mapping(target = "bookName", expression = "java(item.getBook() != null ? item.getBook().getName() : null)")
    OrderItemDto toDto(OrderItem item);
}
