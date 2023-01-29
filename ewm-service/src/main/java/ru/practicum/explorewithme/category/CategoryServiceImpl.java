package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exceptions.AccessDeniedException;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category createCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("This category is already exists");
        }
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryRepository.findAll(page).getContent();
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
        try {
            return categoryRepository.save(categoryToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("This category is already exists");
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId); // check existence of category
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new AccessDeniedException("You can't delete a category while there is at least " +
                    "one event in that category");
        }
        categoryRepository.delete(category);
    }
}
