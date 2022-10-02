package ru.practicum.explorewithme.event.category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    List<Category> getAllCategories(Integer from, Integer size);

    Category getCategoryById(Long categoryId);

    Category editCategory(Category category);

    void deleteCategory(Long categoryId);

}
