package requestflow.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import requestflow.request.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
