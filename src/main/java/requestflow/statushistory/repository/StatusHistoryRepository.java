package requestflow.statushistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import requestflow.statushistory.StatusHistory;

import java.util.Collection;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    Collection<StatusHistory> findAllByRequestIdOrderByChangedAt(Long requestId);
}
