package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    
    // PUBLIC
    @GetMapping("categories")
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("getAllCategoriesPublic");
        return CategoryMapper.toDtoList(categoryService.getAllCategories(from, size));
    }

    @GetMapping(path = "categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("getCategoryByIdPublic - {}", catId);
        return CategoryMapper.toDto(categoryService.getCategoryById(catId));
    }

    // ADMIN
    @PostMapping("admin/categories")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("createCategory - {}", categoryDto.getName());
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toDto(categoryService.createCategory(category));
    }

    @PatchMapping("admin/categories")
    public CategoryDto editCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("editCategory, name - {}", categoryDto.getName());
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toDto(categoryService.editCategory(category));
    }

    @DeleteMapping("admin/categories/{catId}")
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("deleteCategoryById, id - {}", catId);
        categoryService.deleteCategory(catId);
    }
}
