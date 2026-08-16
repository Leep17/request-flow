package requestflow.category.service;

import requestflow.category.Category;
import requestflow.category.dto.NewCategoryDto;
import requestflow.category.dto.UpdateCategoryDto;

import java.util.Collection;

public interface CategoryService {
    Category save(NewCategoryDto newCategoryDto);

    Collection<Category> getAll();

    Category getById(Long id);

    Category updateCategory(Long id, UpdateCategoryDto updateCategoryDto);

    void deleteById(Long id);
}
