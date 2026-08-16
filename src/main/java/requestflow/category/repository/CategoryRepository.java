package requestflow.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import requestflow.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
