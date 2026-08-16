package requestflow.category.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import requestflow.category.Category;
import requestflow.category.repository.CategoryRepository;
import requestflow.category.dto.NewCategoryDto;
import requestflow.category.dto.UpdateCategoryDto;
import requestflow.exception.ConflictException;
import requestflow.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category save(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с названием " + newCategoryDto.getName() + " уже существует");
        }

        Category category = new Category();
        category.setName(newCategoryDto.getName());
        if (newCategoryDto.getDescription() != null && !newCategoryDto.getDescription().isBlank()) {
            category.setDescription(newCategoryDto.getDescription());
        }
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Override
    public Collection<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена"));
    }

    @Transactional
    @Override
    public Category updateCategory(Long id, UpdateCategoryDto updateCategoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена"));
        if (categoryRepository.existsByNameAndIdNot(updateCategoryDto.getName(), id)) {
            throw new ConflictException("Категория с названием " + updateCategoryDto.getName() + " уже существует");
        }
        if (updateCategoryDto.getName() != null) {
           category.setName(updateCategoryDto.getName());
        }

        if (updateCategoryDto.getDescription() != null) {
            category.setDescription(updateCategoryDto.getDescription());
        }
        return category;
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена"));
        categoryRepository.deleteById(id);
    }
}
