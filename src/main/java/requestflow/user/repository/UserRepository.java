package requestflow.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import requestflow.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
