package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.CategoryMapperMapStruct;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapperMapStruct categoryMapper;

    // PUBLIC
    @GetMapping("categories")
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("hit endpoint - getAllCategoriesPublic");
//        return CategoryMapper.toDto(categoryService.getAllCategories(from, size));
        return categoryMapper.toDto(categoryService.getAllCategories(from, size));
    }

    @GetMapping(path = "categories/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        log.info("hit endpoint - getCategoryByIdPublic - {}", categoryId);
        Category category = categoryService.getCategoryById(categoryId);
//        return CategoryMapper.toDto(category);
        return categoryMapper.toDto(category);
    }

    // ADMIN
    @PostMapping("admin/categories")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("hit endpoint - createCategory - {}", categoryDto.getName());
//        Category category = CategoryMapper.toCategory(categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
//        return CategoryMapper.toDto(categoryService.createCategory(category));
        return categoryMapper.toDto(categoryService.createCategory(category));
    }

    @PatchMapping("admin/categories")
    public CategoryDto editCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("hit endpoint - editCategory, name - {}", categoryDto.getName());
//        Category category = CategoryMapper.toCategory(categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
//        return CategoryMapper.toDto(categoryService.editCategory(category));
        return categoryMapper.toDto(categoryService.editCategory(category));
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public void deleteCategoryById(@PathVariable Long categoryId) {
        log.info("hit endpoint - deleteCategoryById, id - {}", categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
