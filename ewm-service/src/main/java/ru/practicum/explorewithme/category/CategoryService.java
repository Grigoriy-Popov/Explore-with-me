package ru.practicum.explorewithme.category;

import java.util.List;

public interface CategoryService {

    Category create(Category category);

    List<Category> getAll(Integer from, Integer size);

    Category getById(Long categoryId);

    Category edit(Category category);

    void deleteById(Long categoryId);

}
