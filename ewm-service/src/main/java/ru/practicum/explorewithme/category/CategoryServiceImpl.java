package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common_dto.PageInfo;
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
    public Category create(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("This category is already exists");
        }
    }

    @Override
    public List<Category> getAll(PageInfo pageInfo) {
        Pageable page = PageRequest.of(pageInfo.getFrom() / pageInfo.getSize(), pageInfo.getSize());
        return categoryRepository.findAll(page).getContent();
    }

    @Override
    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id %d not found", categoryId)));
    }

    @Override
    public Category edit(Category category) {
        Category categoryToUpdate = getById(category.getId());
        categoryToUpdate.setName(category.getName());
        try {
            return categoryRepository.save(categoryToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("This category is already exists");
        }
    }

    @Override
    public void deleteById(Long categoryId) {
        Category category = getById(categoryId); // check existence of category
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new AccessDeniedException("You can't delete a category while there is at least " +
                    "one event in that category");
        }
        categoryRepository.delete(category);
    }
}
