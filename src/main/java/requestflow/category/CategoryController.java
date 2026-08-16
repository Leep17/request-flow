package requestflow.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import requestflow.category.dto.CategoryDto;
import requestflow.category.dto.NewCategoryDto;
import requestflow.category.dto.UpdateCategoryDto;
import requestflow.category.mapper.CategoryMapper;
import requestflow.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.getAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return CategoryMapper.toCategoryDto(categoryService.getById(id));
    }

    @PostMapping
    public CategoryDto saveNewCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryService.save(newCategoryDto));
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@Valid @PathVariable Long id, @RequestBody UpdateCategoryDto updateCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryService.updateCategory(id, updateCategoryDto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

}
