package ru.practicum.explorewithme.category;

import ru.practicum.explorewithme.common_dto.PageInfo;

import java.util.List;

public interface CategoryService {

    Category create(Category category);

    List<Category> getAll(PageInfo pageInfo);

    Category getById(Long categoryId);

    Category edit(Category category);

    void deleteById(Long categoryId);

}
