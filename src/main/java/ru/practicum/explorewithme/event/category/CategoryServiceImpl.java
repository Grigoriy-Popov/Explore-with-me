package ru.practicum.explorewithme.event.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id"));
        return categoryRepository.getAll(page);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id %d not found", categoryId)));
    }

    @Override
    public Category editCategory(Category category) {
        Category categoryToUpdate = getCategoryById(category.getId());
        categoryToUpdate.setName(category.getName());
        return categoryRepository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        getCategoryById(categoryId); // check exists of category
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new IllegalArgumentException("you cannot delete a category while there is at least " +
                    "one event in that category");
        }
        categoryRepository.deleteById(categoryId);
    }
}
