package ru.practicum.explorewithme.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explorewithme.category.Category;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapperMapStruct {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(Collection<Category> categories);

    Category toEntity(CategoryDto categoryDto);
}
