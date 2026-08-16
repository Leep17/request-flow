package requestflow.category.mapper;

import requestflow.category.Category;
import requestflow.category.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt());
    }
}
